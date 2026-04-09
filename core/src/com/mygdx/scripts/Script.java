package com.mygdx.scripts;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import com.mygdx.entities.helpers.ScriptableActor;
import com.mygdx.messages.MSG;
import com.mygdx.resources.enums.DialogueEnum;
import com.mygdx.resources.enums.ScriptEnum;
import com.mygdx.resources.enums.TextureEnum;

public class Script {

    private ArrayList<ScriptAction> actions = new ArrayList<>();
    
    public Script(ScriptEnum script){
        try(var br = new BufferedReader(new FileReader(script.path))){
            br.lines().forEach( line -> {
                int firstSpaceIndex = line.contains(" ") ? line.indexOf(" ") : line.length();
                String func = line.substring(0, firstSpaceIndex);
                String[] args = line.length() != firstSpaceIndex ? line.substring(firstSpaceIndex+1, line.length()).replaceAll(" ", "").split(",") : new String[]{};

                String stringified = line.length() != firstSpaceIndex ? line.substring(firstSpaceIndex+1, line.length()).replaceAll("\"", "") : "";
                actions.add(
                    switch (func){
                        case "MOV" -> new MovAction(Float.parseFloat(args[0]), Float.parseFloat(args[1]), false);
                        case "MOVREL" -> new MovAction(Float.parseFloat(args[0]), Float.parseFloat(args[1]), true);
                        case "ANI" -> new AniAction(TextureEnum.valueOf(args[0]));
                        case "WAIT" -> new WaitAction(Float.parseFloat(args[0]));
                        case "SND" -> new SndAction(MSG.valueOf(args[0]));
                        case "LISTEN" -> args.length == 1 ? new ListenAction(MSG.valueOf(args[0])) : new ListenAction(MSG.valueOf(args[0]), Integer.parseInt(args[1]));
                        case "DO" -> new DoAction(ScriptEnum.valueOf(args[0]));
                        case "TELL" -> new TellAction(DialogueEnum.valueOf(args[0]));
                        case "SAY" -> new SayAction(stringified);
                        case "REMOVE" -> new RemoveAction();
                        case "SKIP" -> new SkipAction();
                        default -> null;
                    }
                );
            }
            );
        } catch(Exception ignored){ignored.printStackTrace();}
    }

    public void proceed(ScriptableActor a){
        if(actions.isEmpty()) return;

        ScriptAction action = actions.get(0);
        actions.remove(0);
        action.perform(a);
    }
}
