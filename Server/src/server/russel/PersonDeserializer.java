package server.russel;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PersonDeserializer implements JsonDeserializer<Person> {
    @Override
    public Person deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        JsonObject jsonObject = json.getAsJsonObject();

        Person person = new Person("", Danger.UNKNOWN);

        try {
            person.setName(jsonObject.get("name").getAsString());
        }catch (Exception e) {
            person.setName("Noname");
        }
        try {
            person.setAge(jsonObject.get("age").getAsInt());
        }catch (Exception e) {
            person.setAge(18);
        }
        try {
            person.setDanger(Danger.fromValue(jsonObject.get("danger").getAsString().toUpperCase()));
        }catch (Exception e) {
            person.setDanger(Danger.UNKNOWN);
        }
        try {
            JsonArray equip = jsonObject.getAsJsonArray("equip");
            for(JsonElement e : equip) {
                Parachute parachute = context.deserialize(e, Parachute.class);
                if (parachute.getRarity() == null) parachute.setRarity(Rarity.COMMON);
                if (parachute.getDanger() == null) parachute.setDanger(Danger.UNKNOWN);
                person.putOn(parachute);
            }
        }catch (Exception e) {
            person.setEquip(new ArrayList<>());
        }


        return person;
    }
}
