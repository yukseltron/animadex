package com.animadex.animadex;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {

    public String parse_summary(String js) {
        try{
            JSONObject jObject = new JSONObject(js);
            JSONArray jArray = jObject.getJSONArray(js);
            String first = (String) jArray.get(0);
            JSONObject object = new JSONObject(first);
            return (String) object.get("summary");
        }catch(Exception e){
            e.getStackTrace();
            return null;
        }
    }

    public String parse_tag(String js) {
        try{
            JSONObject jObject = new JSONObject(js);
            JSONArray jArray = jObject.getJSONArray(js);
            String first = (String) jArray.get(0);
            JSONObject object = new JSONObject(first);
            return (String) object.get("tag");
        }catch(Exception e) {
            e.getStackTrace();
            return null;
        }
    }

    public static void main(String args[]){
        String js = "[{\"summary\": \"Snakes are elongated, legless, carnivorous reptiles of the suborder Serpentes that can be distinguished from legless lizards by their lack of eyelids and external ears. Like all squamates, snakes are ectothermic, amniote vertebrates covered in overlapping scales. Many species of snakes have skulls with several more joints than their lizard ancestors, enabling them to swallow prey much larger than their heads with their highly mobile jaws. To accommodate their narrow bodies, snakes' paired organs (such as kidneys) appear one in front of the other instead of side by side, and most have only one functional lung. Some species retain a pelvic girdle with a pair of vestigial claws on either side of the cloaca.\\nLiving snakes are found on every continent except Antarctica, and on most smaller land masses; exceptions include some large islands, such as Ireland and New Zealand, and many small islands of the Atlantic and central Pacific. Additionally, sea snakes are widespread throughout the Indian and Pacific Oceans. More than 20 families are currently recognized, comprising about 500 genera and about 3,400 species. They range in size from the tiny, 10 cm-long thread snake to the reticulated python of up to 6.95 meters (22.8 ft) in length. The fossil species Titanoboa cerrejonensis was 13 meters (43 ft) long. Snakes are thought to have evolved from either burrowing or aquatic lizards, perhaps during the Jurassic period, with the earliest known fossils dating to between 143 and 167 Ma ago. The diversity of modern snakes appeared during the Paleocene period (c 66 to 56 Ma ago). The oldest preserved descriptions of snakes can be found in the Brooklyn Papyrus.\\nMost species are nonvenomous and those that have venom use it primarily to kill and subdue prey rather than for self-defense. Some possess venom potent enough to cause painful injury or death to humans. Nonvenomous snakes either swallow prey alive or kill by constriction.\", \"tag\": \"snake\", \"linnean\": 11, \"probs\": 0.9999876618385315, \"id\": \"ai_4SfD4KJz\"}, {\"summary\": \"The Viperidae (vipers) are a family of venomous snakes found all over the world, except in Antarctica, Australia, New Zealand, Madagascar, Hawaii, various other isolated islands, and north of the Arctic Circle. All have relatively long, hinged fangs that permit deep penetration and injection of venom. Four subfamilies are currently recognised. They are also known as viperids.\", \"tag\": \"viper\", \"linnean\": 8, \"probs\": 0.9929381608963013, \"id\": \"ai_8W9T6GKb\"}, {\"summary\": \"Reptiles are a group (Reptilia) of tetrapod animals comprising today's turtles, crocodilians, snakes, amphisbaenians, lizards, tuatara, and their extinct relatives. The study of these traditional reptile groups, historically combined with that of modern amphibians, is called herpetology. Birds are also often included as a sub-group of reptiles by modern scientists.\\nThe earliest known proto-reptiles originated around 312 million years ago during the Carboniferous period, having evolved from advanced reptiliomorph tetrapods that became increasingly adapted to life on dry land. Some early examples include the lizard-like Hylonomus and Casineria. In addition to the living reptiles, there are many diverse groups that are now extinct, in some cases due to mass extinction events. In particular, the K\\u2013Pg extinction wiped out the pterosaurs, plesiosaurs, ornithischians, and sauropods, as well as many species of theropods (e.g. tyrannosaurids and dromaeosaurids), crocodyliforms, and squamates (e.g. mosasaurids).\\nModern reptiles inhabit every continent with the exception of Antarctica. Several living subgroups are recognized:\\nTestudines (turtles, terrapins and tortoises): approximately 400 species\\nSphenodontia (tuatara from New Zealand): 1 species \\nSquamata (lizards, snakes, and worm lizards): over 9,600 species\\nCrocodilia (crocodiles, gavials, caimans, and alligators): 25 species\\nBecause some reptiles are more closely related to birds than they are to other reptiles (crocodiles are more closely related to birds than they are to lizards), many modern scientists prefer to make Reptilia a monophyletic grouping and so also include the birds, which today contain over 10,000 species.\\nReptiles are tetrapod vertebrates, creatures that either have four limbs or, like snakes, are descended from four-limbed ancestors. Unlike amphibians, reptiles do not have an aquatic larval stage. Most reptiles are oviparous, although several species of squamates are viviparous, as were some extinct aquatic clades\\u200a\\u2014\\u200athe fetus develops within the mother, contained in a placenta rather than an eggshell. As amniotes, reptile eggs are surrounded by membranes for protection and transport, which adapt them to reproduction on dry land. Many of the viviparous species feed their fetuses through various forms of placenta analogous to those of mammals, with some providing initial care for their hatchlings. Extant reptiles range in size from a tiny gecko, Sphaerodactylus ariasae, which can grow up to 17 mm (0.7 in) to the saltwater crocodile, Crocodylus porosus, which may reach 6 m (19.7 ft) in length and weigh over 1,000 kg (2,200 lb).\", \"tag\": \"reptile\", \"linnean\": 6, \"probs\": 0.9998005628585815, \"id\": \"ai_j59dWjhs\"}]";

        try{
            JSONObject jObject = new JSONObject(js);
            JSONArray jArray = jObject.getJSONArray(js);
            String first = (String) jArray.get(0);
            JSONObject object = new JSONObject(first);
            object.get("summary");
        }catch(Exception e){
            e.getStackTrace();
        }
    }

}
