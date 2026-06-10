// Placeholder classes for Haxe dependencies
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class PlayState {
    public static PlayState instance = new PlayState(); // Singleton instance
    public Stage currentStage = new Stage();
    public Camera camGame = new Camera();
    public Camera camHUD = new Camera();
    public Camera camCutscene = new Camera();
    public boolean isMinimalMode = false;
    public boolean isInCutscene = false;
    public Song currentSong = new Song();

    public Character getBoyfriend() { return new Character(); }
    public Character getGirlfriend() { return new Character(); }
    public Character getDad() { return new Character(); }
    public void addCharacter(Character character, Object type) { /* ... */ }
}

class Stage {
    public StageProp getNamedProp(String propName) {
        return new StageProp();
    }
    public void addCharacter(Character character, Object type) { /* ... */ }
}

class StageProp {
    public double alpha;
}

class Camera {
    public void stopFlash() { /* ... */ }
    public void flash(int color, double duration) { /* ... */ }
}

class FlxEase {
    public static Object quadOut; // Placeholder for easing function
}

class FlxTween {
    public static void tween(Object target, Object properties, double duration, Object options) {
        // Placeholder for tweening logic
    }
    public void cancel() { /* ... */ }
    public boolean active = false;
}

class ScriptedModule {
    public ScriptedModule(String name) {
    }

    public void onSongRetry(ScriptEvent event) {
    }

    public void onSongEvent(SongEventScriptEvent scriptEvent) {
    }
    public void onGameOver(ScriptEvent event) {
    }
    public void onStateChangeBegin(Object event) { /* ... */ }
    public void onSubStateCloseBegin(SubStateScriptEvent event) { /* ... */ }
    public void onSongLoaded(SongLoadScriptEvent event) { /* ... */ }
}

class ScriptedSongEvent {
    public ScriptedSongEvent(String name) {
    }

    public void handleEvent(Object data) {
    }
}

class ScriptEvent {
}

class SongEventScriptEvent extends ScriptEvent {
    public EventData eventData;

    static class EventData {
        public String eventKind;
        public Value value;

        static class Value {
            public String propName;
            public double alpha;
            public double duration;
            public String newchar;
            public String character;
            public int inout;
            public int object;
            public double seconds;
        }
    }
}

class StateChangeScriptEvent extends ScriptEvent {
}

class SubStateScriptEvent extends ScriptEvent {
}

class SongLoadScriptEvent extends ScriptEvent {
}

class ReflectUtil {
    public static String getClassNameOf(Object obj) { return ""; }
}

class CharacterDataParser {
    public static Character parseCharacterData(String data) { return new Character(); }
    public static Map<Integer, Character> characterCache = new HashMap<>();
    public static Character fetchCharacter(String name) { return new Character(); }
}

class Character {
    public void destroy() { /* ... */ }
    public void set_characterType(Object type) { /* ... */ }
    public void initHealthIcon(boolean isDad) { /* ... */ }
}

class CharacterType {
    public static Object BF; // Placeholder for enum value
    public static Object GF; // Placeholder for enum value
    public static Object DAD; // Placeholder for enum value
}

class Preferences {
    public static boolean flashingLights = true;
}

class Conductor {
    public static Conductor instance = new Conductor();
    public double stepLengthMs = 100.0;
}

class FlxG {
    public static FlxG camera = new FlxG();
    public static int width = 1280; // Placeholder
    public static int height = 720; // Placeholder
    public static FlxState state = new FlxState();
}

class FlxState {
    public Object subState;
}

class FlxSprite {
    public FlxSprite(int x, int y) { /* ... */ }
    public FlxSprite makeGraphic(int width, int height, int color) { return this; }
    public int zIndex;
    public List<Camera> cameras = new ArrayList<>();
    public double alpha;
}

class Song {
    public String id = "";
}
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class HUDFadeFixes extends ScriptedSongEvent
{
      var Bool shouldFixFadeEvent = true;

  public HUDFadeEvent(){
    super("HUDFadeFixes");
  }

  private void onUpdate(event)
{
if (PlayState.instance == null) return;
if (PlayState.instance.opponentStrumline == null) return;
if (PlayState.instance.playerStrumline == null) return;
for (i in PlayState.instance.opponentStrumline.notes)
{
i.alpha = PlayState.instance.opponentStrumline.alpha;
}
for (i in PlayState.instance.opponentStrumline.holdNotes)
{
i.alpha = PlayState.instance.opponentStrumline.alpha;
}
for (i in PlayState.instance.playerStrumline.notes)
{
i.alpha = PlayState.instance.playerStrumline.alpha;
}
for (i in PlayState.instance.playerStrumline.noteHoldCovers)
{
i.alpha = PlayState.instance.playerStrumline.alpha;  
}
for (i in PlayState.instance.playerStrumline.holdNotes)
{
i.alpha = PlayState.instance.playerStrumline.alpha;
}
}

    private void onGameOver(ScriptEvent event){
    super.onGameOver(event);
    if (PlayState.instance.camHUD != null && shouldFixFadeEvent) PlayState.instance.camHUD.stopTween();
  }

  private void onSongRetry(ScriptEvent event){
    super.onSongRetry(event);
    if (PlayState.instance.camHUD != null && shouldFixFadeEvent) PlayState.instance.camHUD.stopTween();
  }
}

public class HUDFadeEvent extends ScriptedSongEvent 
{
  private void new() {
    super("HUDFadeEvent");
  }

  public String eventTitle = "HUD Fade";

  public Int DEFAULT_HUD = 0;
  public Float DEFAULT_DURATION = 4.0;
  public Bool DEFAULT_SHOULDFADEIN = false;

    override private void handleEvent(data) void {
    if (PlayState.instance == null || PlayState.instance.currentStage == null) return; // used to know if we"re in a song
    if (PlayState.instance.isMinimalMode) return;

    var Float duration = data.get("duration") != null ? ((Number)data.get("duration")).floatValue() : DEFAULT_DURATION;
    var Bool shouldFadeIn = data.get("shouldFadeIn") != null ? (Boolean)data.get("shouldFadeIn") : DEFAULT_SHOULDFADEIN;
    var Int hud = data.get("hud") != null ? ((Number)data.get("hud")).intValue() : DEFAULT_HUD;
    var durSeconds = Conductor.instance.stepLengthMs * duration / 1000;




        switch (hud) {
      case 0: // all 
        hud = 0;
      case 1: // hp bar
        hud = 1;
      case 2: // the shit AKA the notes
        hud = 2;
        }
  
    if (duration < 0) {
      return;
    }
    var daNote = 


    if (!shouldFadeIn)
    {
      if (hud == 0){
        FlxTween.tween(PlayState.instance.camHUD, new Object() { public double alpha = 0.0; }, durSeconds, {ease: FlxEase.quadInOut});
        return;
      } else{
        if (hud == 1){
      FlxTween.tween(PlayState.instance.healthBar, new Object() { public double alpha = 0.0; }, durSeconds, {ease: FlxEase.quadInOut});
      FlxTween.tween(PlayState.instance.healthBarBG, new Object() { public double alpha = 0.0; }, durSeconds, {ease: FlxEase.quadInOut});
      FlxTween.tween(PlayState.instance.iconP1, new Object() { public double alpha = 0.0; }, durSeconds, {ease: FlxEase.quadInOut});
      FlxTween.tween(PlayState.instance.iconP2, new Object() { public double alpha = 0.0; }, durSeconds, {ease: FlxEase.quadInOut});
      return; 
        } else{
          if (hud == 2){
           FlxTween.tween(PlayState.instance.opponentStrumline.notes, new Object() { public double alpha = 0.0; }, durSeconds, {ease: FlxEase.quadInOut});
           FlxTween.tween(PlayState.instance.opponentStrumline.holdNotes, new Object() { public double alpha = 0.0; }, durSeconds, {ease: FlxEase.quadInOut});
           FlxTween.tween(PlayState.instance.playerStrumline.notes, new Object() { public double alpha = 0.0; }, durSeconds, {ease: FlxEase.quadInOut});
           FlxTween.tween(PlayState.instance.playerStrumline, new Object() { public double alpha = 0.0; }, durSeconds, {ease: FlxEase.quadInOut});
           FlxTween.tween(PlayState.instance.opponentStrumline, new Object() { public double alpha = 0.0; }, durSeconds, {ease: FlxEase.quadInOut});
           FlxTween.tween(PlayState.instance.playerStrumline.holdNotes, new Object() { public double alpha = 0.0; }, durSeconds, {ease: FlxEase.quadInOut}); 
           FlxTween.tween(PlayState.instance.playerStrumline.strumlineNotes, new Object() { public double alpha = 0.0; }, durSeconds, {ease: FlxEase.quadInOut});
           FlxTween.tween(PlayState.instance.opponentStrumline.strumlineNotes, new Object() { public double alpha = 0.0; }, durSeconds, {ease: FlxEase.quadInOut});
           FlxTween.tween(PlayState.instance.opponentStrumline.notes, new Object() { public double alpha = 0.0; }, durSeconds, {ease: FlxEase.quadInOut});
           FlxTween.tween(PlayState.instance.opponentStrumline.holdNotes, new Object() { public double alpha = 0.0; }, durSeconds, {ease: FlxEase.quadInOut});
           FlxTween.tween(PlayState.instance.playerStrumline.notes, new Object() { public double alpha = 0.0; }, durSeconds, {ease: FlxEase.quadInOut});
           FlxTween.tween(PlayState.instance.playerStrumline, new Object() { public double alpha = 0.0; }, durSeconds, {ease: FlxEase.quadInOut});
           FlxTween.tween(PlayState.instance.opponentStrumline, new Object() { public double alpha = 0.0; }, durSeconds, {ease: FlxEase.quadInOut});
           FlxTween.tween(PlayState.instance.playerStrumline.holdNotes, new Object() { public double alpha = 0.0; }, durSeconds, {ease: FlxEase.quadInOut}); 
           FlxTween.tween(PlayState.instance.playerStrumline.strumlineNotes, new Object() { public double alpha = 0.0; }, durSeconds, {ease: FlxEase.quadInOut});
           FlxTween.tween(PlayState.instance.opponentStrumline.strumlineNotes, new Object() { public double alpha = 0.0; }, durSeconds, {ease: FlxEase.quadInOut});
           FlxTween.tween(PlayState.instance.playerStrumline.noteHoldCovers, new Object() { public double alpha = 0.0; }, durSeconds, {ease: FlxEase.quadInOut});
           return;
          }
        }
      }
      
    }  else {
      if (hud == 0){
        FlxTween.tween(PlayState.instance.camHUD, new Object() { public double alpha = 1.0; }, durSeconds, {ease: FlxEase.quadInOut});
        return;
      } else{
        if (hud == 1){
      FlxTween.tween(PlayState.instance.healthBar, new Object() { public double alpha = 1.0; }, durSeconds, {ease: FlxEase.quadInOut});
      FlxTween.tween(PlayState.instance.healthBarBG, new Object() { public double alpha = 1.0; }, durSeconds, {ease: FlxEase.quadInOut});
      FlxTween.tween(PlayState.instance.iconP1, new Object() { public double alpha = 1.0; }, durSeconds, {ease: FlxEase.quadInOut});
      FlxTween.tween(PlayState.instance.iconP2, new Object() { public double alpha = 1.0; }, durSeconds, {ease: FlxEase.quadInOut});
      return; 
        } else{
          if (hud == 2){
           FlxTween.tween(PlayState.instance.opponentStrumline.notes, new Object() { public double alpha = 1.0; }, durSeconds, {ease: FlxEase.quadInOut});
           FlxTween.tween(PlayState.instance.opponentStrumline.holdNotes, new Object() { public double alpha = 1.0; }, durSeconds, {ease: FlxEase.quadInOut});
           FlxTween.tween(PlayState.instance.playerStrumline.notes, new Object() { public double alpha = 1.0; }, durSeconds, {ease: FlxEase.quadInOut});
           FlxTween.tween(PlayState.instance.playerStrumline, new Object() { public double alpha = 1.0; }, durSeconds, {ease: FlxEase.quadInOut});
           FlxTween.tween(PlayState.instance.opponentStrumline, new Object() { public double alpha = 1.0; }, durSeconds, {ease: FlxEase.quadInOut});
           FlxTween.tween(PlayState.instance.playerStrumline.holdNotes, new Object() { public double alpha = 1.0; }, durSeconds, {ease: FlxEase.quadInOut}); 
           FlxTween.tween(PlayState.instance.playerStrumline.strumlineNotes, new Object() { public double alpha = 1.0; }, durSeconds, {ease: FlxEase.quadInOut});
           FlxTween.tween(PlayState.instance.opponentStrumline.strumlineNotes, new Object() { public double alpha = 1.0; }, durSeconds, {ease: FlxEase.quadInOut});
           FlxTween.tween(PlayState.instance.playerStrumline.noteHoldCovers, new Object() { public double alpha = 1.0; }, durSeconds, {ease: FlxEase.quadInOut});
           return;
          }
        }
      }
    }
      
  }


  
    @Override
    public void getTitle() {
    return eventTitle;
  }


    override private void getEventSchema(){
    return new ArrayList<Map<String, Object>>() { { add(new HashMap<String, Object>() { { put("name", "duration"); put("title", "Duration"); put("defaultValue", 4.0); put("step", 1.0); put("min", 0.1); put("type", "float"); put("units", "steps"); } }); add(new HashMap<String, Object>() { { put("name", "hud"); put("title", "HUD"); put("defaultValue", 0); put("type", "enum"); put("keys", new HashMap<String, Object>() { { put("Both", 0); put("Health Bar", 1); put("Notes", 2); } }); } }); add(new HashMap<String, Object>() { { put("name", "shouldFadeIn"); put("title", "Fade in',
        defaultValue: false,
        type: "); } }); } };
  }
}