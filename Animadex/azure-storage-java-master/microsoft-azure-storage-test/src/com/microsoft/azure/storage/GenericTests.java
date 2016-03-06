/**
 * Copyright Microsoft Corporation
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.microsoft.azure.storage;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.microsoft.azure.storage.TestRunners.CloudTests;
import com.microsoft.azure.storage.TestRunners.DevFabricTests;
import com.microsoft.azure.storage.TestRunners.DevStoreTests;
import com.microsoft.azure.storage.TestRunners.SlowTests;
import com.microsoft.azure.storage.blob.BlobOutputStream;
import com.microsoft.azure.storage.blob.BlobRequestOptions;
import com.microsoft.azure.storage.blob.BlobTestHelper;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.core.SR;
import com.microsoft.azure.storage.core.Utility;
import com.microsoft.azure.storage.queue.CloudQueue;
import com.microsoft.azure.storage.queue.CloudQueueClient;
import com.microsoft.azure.storage.table.CloudTable;
import com.microsoft.azure.storage.table.CloudTableClient;

@Category({ DevFabricTests.class, DevStoreTests.class, CloudTests.class })
public class GenericTests {

    /**
     * ReadTimeout must always be explicitly set on HttpUrlConnection to avoid a bug in JDK 6. In certain cases this
     * bug causes an immediate SocketException to be thrown indicating that the read timed out even if ReadTimeout was
     * not set. The SocketException is retried and can cause server-side errors to be returned (in this case, an
     * InvalidBlockList error). This tests to make sure we are setting read timeout so that this issue does not occur.
     * 
     * @see {@link BaseRequest}
     * @throws URISyntaxException
     * @throws StorageException
     * @throws IOException
     */
    @Test
    @Category({ DevFabricTests.class, DevStoreTests.class, SlowTests.class })
    public void testReadTimeoutIssue() throws URISyntaxException, StorageException, IOException {
        // part 1
        byte[] buffer = BlobTestHelper.getRandomBuffer(1 * 1024 * 1024);

        // set the maximum execution time
        BlobRequestOptions options = new BlobRequestOptions();
        options.setMaximumExecutionTimeInMs(5000);

        CloudBlobClient blobClient = TestHelper.createCloudBlobClient();
        CloudBlobContainer container = blobClient.getContainerReference(generateRandomContainerName());

        String blobName = "testBlob";
        final CloudBlockBlob blockBlobRef = container.getBlockBlobReference(blobName);
        blockBlobRef.setStreamWriteSizeInBytes(1 * 1024 * 1024);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer);
        BlobOutputStream blobOutputStream = null;

        try {
            container.createIfNotExists();
            blobOutputStream = blockBlobRef.openOutputStream(null, options, null);
            try {
                blobOutputStream.write(inputStream, buffer.length);
            }
            finally {
                blobOutputStream.close();
            }
            assertTrue(blockBlobRef.exists());
        }
        finally {
            inputStream.close();
            container.deleteIfExists();
        }

        // part 2
        int length2 = 10 * 1024 * 1024;
        byte[] uploadBuffer2 = BlobTestHelper.getRandomBuffer(length2);

        CloudBlobClient blobClient2 = TestHelper.createCloudBlobClient();
        CloudBlobContainer container2 = blobClient2.getContainerReference(generateRandomContainerName());

        String blobName2 = "testBlob";
        final CloudBlockBlob blockBlobRef2 = container2.getBlockBlobReference(blobName2);

        ByteArrayInputStream inputStream2 = new ByteArrayInputStream(uploadBuffer2);

        try {
            container2.createIfNotExists();

            blockBlobRef2.upload(inputStream2, length2);
        }
        finally {
            inputStream2.close();
            container2.deleteIfExists();
        }
    }

    /**
     * Make sure that if a request throws an error when it is being built that the request is not sent.
     * 
     * @throws URISyntaxException
     * @throws StorageException
     */
    @Test
    public void testExecutionEngineErrorHandling() throws URISyntaxException, StorageException {
        CloudBlobContainer container = BlobTestHelper.getRandomContainerReference();
        try {
            final ArrayList<Boolean> callList = new ArrayList<Boolean>();

            OperationContext opContext = new OperationContext();
            opContext.getSendingRequestEventHandler().addListener(new StorageEvent<SendingRequestEvent>() {
                // insert a metadata element with an empty value
                @Override
                public void eventOccurred(SendingRequestEvent eventArg) {
                    callList.add(true);
                }
            });

            container.getMetadata().put("key", " "); // invalid value
            try {
                container.uploadMetadata(null, null, opContext);
                fail(SR.METADATA_KEY_INVALID);
            }
            catch (StorageException e) {
                // make sure a request was not sent
                assertEquals(0, callList.size());

                assertEquals(SR.METADATA_VALUE_INVALID, e.getMessage());
            }
        }
        finally {
            container.deleteIfExists();
        }
    }

    @Test
    public void testUserAgentString() throws URISyntaxException, StorageException {
        // Test with a blob request
        CloudBlobClient blobClient = TestHelper.createCloudBlobClient();
        CloudBlobContainer container = blobClient.getContainerReference("container1");
        OperationContext sendingRequestEventContext = new OperationContext();
        sendingRequestEventContext.getSendingRequestEventHandler().addListener(new StorageEvent<SendingRequestEvent>() {

            @Override
            public void eventOccurred(SendingRequestEvent eventArg) {
                assertEquals(
                        Constants.HeaderConstants.USER_AGENT_PREFIX
                                + "/"
                                + Constants.HeaderConstants.USER_AGENT_VERSION
                                + " "
                                + String.format(Utility.LOCALE_US, "(JavaJRE %s; %s %s)",
                                        System.getProperty("java.version"),
                                        System.getProperty("os.name").replaceAll(" ", ""),
                                        System.getProperty("os.version")), ((HttpURLConnection) eventArg
                                .getConnectionObject()).getRequestProperty(Constants.HeaderConstants.USER_AGENT));
            }
        });
        container.exists(null, null, sendingRequestEventContext);

        // Test with a queue request
        CloudQueueClient queueClient = TestHelper.createCloudQueueClient();
        CloudQueue queue = queueClient.getQueueReference("queue1");
        queue.exists(null, sendingRequestEventContext);

        // Test with a table request
        CloudTableClient tableClient = TestHelper.createCloudTableClient();
        CloudTable table = tableClient.getTableReference("table1");
        table.exists(null, sendingRequestEventContext);
    }

    @Test
    public void testUserHeaders() throws URISyntaxException, StorageException {
        CloudBlobClient blobClient = TestHelper.createCloudBlobClient();
        CloudBlobContainer container = blobClient.getContainerReference("container1");
        OperationContext context = new OperationContext();

        // no user headers
        container.exists(null, null, context);

        // add user headers
        HashMap<String, String> userHeaders = new HashMap<String, String>();
        userHeaders.put("x-ms-foo", "bar");
        userHeaders.put("x-ms-hello", "value");
        context.setUserHeaders(userHeaders);
        StorageEvent<SendingRequestEvent> event = new StorageEvent<SendingRequestEvent>() {

            @Override
            public void eventOccurred(SendingRequestEvent eventArg) {
                HttpURLConnection connection = (HttpURLConnection) eventArg.getConnectionObject();
                assertNotNull(connection.getRequestProperty("x-ms-foo"));
                assertNotNull(connection.getRequestProperty("x-ms-hello"));
            }
        };

        context.getSendingRequestEventHandler().addListener(event);
        container.exists(null, null, context);

        // clear user headers
        userHeaders.clear();
        context.getSendingRequestEventHandler().removeListener(event);
        context.setUserHeaders(userHeaders);
        context.getSendingRequestEventHandler().addListener(new StorageEvent<SendingRequestEvent>() {

            @Override
            public void eventOccurred(SendingRequestEvent eventArg) {
                HttpURLConnection connection = (HttpURLConnection) eventArg.getConnectionObject();
                assertNull(connection.getRequestProperty("x-ms-foo"));
                assertNull(connection.getRequestProperty("x-ms-hello"));
            }
        });

        container.exists(null, null, context);
    }

    @Test
    public void testNullRetryPolicy() throws URISyntaxException, StorageException {
        CloudBlobClient blobClient = TestHelper.createCloudBlobClient();
        CloudBlobContainer container = blobClient.getContainerReference("container1");

        blobClient.getDefaultRequestOptions().setRetryPolicyFactory(null);
        container.exists();
    }

    @Test
    public void testDateStringParsingWithRounding() throws ParseException {
        String fullDateString = "1999-12-31T23:59:45.1234567Z";
        SimpleDateFormat testFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
        Date milliDate = testFormat.parse("1999-12-31T23:59:45.123 -0000");
        assertEquals(milliDate, Utility.parseDate(fullDateString));

        fullDateString = "1999-04-30T23:59:55.9876Z";
        long millisSinceEpoch = 925516795987L;

        Date deciDate = Utility.parseDate(fullDateString.replace("876Z", "Z"));
        assertEquals(deciDate.getTime(), (millisSinceEpoch / 100) * 100);

        Date centiDate = Utility.parseDate(fullDateString.replace("76Z", "Z"));
        assertEquals(centiDate.getTime(), (millisSinceEpoch / 10) * 10);

        milliDate = Utility.parseDate(fullDateString);
        assertEquals(milliDate.getTime(), millisSinceEpoch);
    }
    
    @Test
    public void testDateStringParsing() throws ParseException {
        // 2014-12-07T09:15:12.123Z  from Java
        testDate("2014-12-07T09:15:12.123Z", 1417943712123L, 0, false, false);

        // 2015-01-14T14:53:32.800Z  from Java
        testDate("2015-01-14T14:53:32.800Z", 1421247212800L, 0, false, false);

        // 2014-11-29T22:55:21.9876543Z  from .Net
        testDate("2014-11-29T22:55:21.9876543Z", 1417301721987L, 6543, false, false);

        // 2015-02-14T03:11:13.0000229Z  from .Net
        testDate("2015-02-14T03:11:13.0000229Z", 1423883473000L, 229, false, false);
    }

    @Test
    public void testDateStringParsingCrossVersion() throws ParseException {
        // 2014-12-07T09:15:12.123Z  from Java, milliseconds are incorrectly left-padded
        testDate("2014-12-07T09:15:12.0000123Z", 1417943712123L, 0, true, false);

        // 2015-01-14T14:53:32.800Z  from Java, milliseconds are incorrectly left-padded
        testDate("2015-01-14T14:53:32.0000800Z", 1421247212800L, 0, true, false);

        // 2014-11-29T22:55:21.9876543Z  from .Net
        testDate("2014-11-29T22:55:21.9876543Z", 1417301721987L, 6543, true, false);

        // 2015-02-14T03:11:13.0000229Z  from .Net
        testDate("2015-02-14T03:11:13.0000229Z", 1423883473000L, 229, true, false);
    }
    
    @Test
    public void testDateStringParsingWithBackwardCompatibility() throws ParseException {
        // 2014-12-07T09:15:12.123Z  from Java
        testDate("2014-12-07T09:15:12.123Z", 1417943712123L, 0, false, true);

        // 2015-01-14T14:53:32.800Z  from Java
        testDate("2015-01-14T14:53:32.800Z", 1421247212800L, 0, false, true);

        // 2014-11-29T22:55:21.9876543Z  from .Net
        testDate("2014-11-29T22:55:21.9876543Z", 1417301721987L, 6543, false, true);

        // 2015-02-14T03:11:13.0000229Z  from .Net
        testDate("2015-02-14T03:11:13.0000229Z", 1423883473000L, 229, false, true);
    }

    @Test
    public void testDateStringParsingCrossVersionWithBackwardCompatibility() throws ParseException {
        // 2014-12-07T09:15:12.123Z  from Java, milliseconds are incorrectly left-padded
        testDate("2014-12-07T09:15:12.0000123Z", 1417943712123L, 0, true, true);

        // 2015-01-14T14:53:32.800Z  from Java, milliseconds are incorrectly left-padded
        testDate("2015-01-14T14:53:32.0000800Z", 1421247212800L, 0, true, true);

        // 2014-11-29T22:55:21.9876543Z  from .Net
        testDate("2014-11-29T22:55:21.9876543Z", 1417301721987L, 6543, true, true);

        // 2015-02-14T03:11:13.0000229Z  from .Net
        testDate("2015-02-14T03:11:13.0000229Z", 1423883473000L, 229, true, true);
    }

    private static void testDate(final String dateString, final long intendedMilliseconds, final int ticks,
            final boolean writtenPre2, final boolean dateBackwardCompatibility) {
        assertTrue(ticks >= 0);     // ticks is non-negative
        assertTrue(ticks <= 9999);  // ticks do not overflow into milliseconds
        long expectedMilliseconds = intendedMilliseconds;
        
        if (dateBackwardCompatibility && (intendedMilliseconds % 1000 == 0) && (ticks < 1000)) {
            // when no milliseconds are present dateBackwardCompatibility causes up to 3 digits of ticks
            // to be read as milliseconds
            expectedMilliseconds += ticks;
        } else if (writtenPre2 && !dateBackwardCompatibility && (ticks == 0)) {
            // without DateBackwardCompatibility, milliseconds stored by Java prior to 2.0.0 are lost
            expectedMilliseconds -= expectedMilliseconds % 1000;
        }
        
        assertEquals(expectedMilliseconds, Utility.parseDate(dateString, dateBackwardCompatibility).getTime());
    }

    @Test
    public void testDateStringFormatting() {
        String fullDateString = "2014-12-07T09:15:12.123Z";
        String outDateString = Utility.getJavaISO8601Time(Utility.parseDate(fullDateString));
        assertEquals(fullDateString, outDateString);
        
        fullDateString = "2015-01-14T14:53:32.800Z";
        outDateString = Utility.getJavaISO8601Time(Utility.parseDate(fullDateString));
        assertEquals(fullDateString, outDateString);
        
        // Ensure that trimming of trailing zeroes by the service does not affect this
        fullDateString = "2015-01-14T14:53:32.8Z";
        outDateString = Utility.getJavaISO8601Time(Utility.parseDate(fullDateString));
        fullDateString = fullDateString.replace("Z", "00Z");
        assertEquals(fullDateString, outDateString);
        
        // Ensure that trimming of trailing zeroes by the service does not affect this
        // even with dateBackwardCompatibility
        fullDateString = "2015-01-14T14:53:32.0000800Z";
        outDateString = Utility.getJavaISO8601Time(Utility.parseDate(fullDateString, true));
        fullDateString = "2015-01-14T14:53:32.800Z";
        assertEquals(fullDateString, outDateString);
    }

    private static String generateRandomContainerName() {
        String containerName = "container" + UUID.randomUUID().toString();
        return containerName.replace("-", "");
    }
}
