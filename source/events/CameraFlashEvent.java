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

// import flixel.util.FlxColor; // A dream wish for now



public class CameraFlashFixes extends ScriptedModule {
  var Bool shouldFixFlashEvent = true;

  public CameraFlashEvent(){
    super("extra-events-cameraFlashFixes");
  }
  
  private void onGameOver(ScriptEvent event){
    super.onGameOver(event);
    if (PlayState.instance.camGame != null && shouldFixFlashEvent) PlayState.instance.camGame.stopFlash();
    if (PlayState.instance.camHUD != null && shouldFixFlashEvent) PlayState.instance.camHUD.stopFlash();
  }

  private void onSongRetry(ScriptEvent event){
    super.onSongRetry(event);
    if (PlayState.instance.camGame != null && shouldFixFlashEvent) PlayState.instance.camGame.stopFlash();
    if (PlayState.instance.camHUD != null && shouldFixFlashEvent) PlayState.instance.camHUD.stopFlash();
  }
}

public class CameraFlashEvent extends ScriptedSongEvent {
  
  private void new() {
      super("extra-events-cameraFlashEvent");
  }

  public String eventTitle = "Camera Flash";
  /*
  * white = 0xFFFFFFFF
  * red = 0xFFFF0000
  */
  public Float DEFAULT_DURATION = 1.0;
  public Bool DEFAULT_APPLYTOHUD = false; // self-explanatory, default to false for players who just want to read the notes like me
  public Int DEFAULT_COLOR = 12; // Black

  override private void handleEvent(data) void {
    if (PlayState.instance == null || PlayState.instance.currentStage == null) return;
    if (PlayState.instance.isMinimalMode) return;

    if (!Preferences.flashingLights) return;

    var Float duration = data.get("duration") != null ? ((Number)data.get("duration")).floatValue() : DEFAULT_DURATION;
    var Bool applyToHud = data.get("applyToHud") != null ? (Boolean)data.get("applyToHud") : DEFAULT_APPLYTOHUD;
    var Int color = data.get("color") != null ? ((Number)data.get("color")).intValue() : DEFAULT_COLOR;

    switch (color) {
      case 0: // Black
        color = 0xFF000000;
      case 1: // Blue
        color = 0xFF0000FF;
      case 2: // Brown
        color = 0xFFA52A2A;
      case 3: // Cyan
        color = 0xFF00FFFF;
      case 4: // Gray
        color = 0xFF808080;
      case 5: // Green
        color = 0xFF008000;
      case 6: // Lime
        color = 0xFF00FF00;
      case 7: // Magenta
        color = 0xFFFF00FF;
      case 8: // Orange
        color = 0xFFFFA500;
      case 9: // Purple
        color = 0xFF800080;
      case 10: // Red
        color = 0xFFFF0000;
      case 11: // Transparent
        color = 0x00000000;
      case 12: // White
        color = 0xFFFFFFFF;
      case 13: // Yellow
        color = 0xFFFFFF00;

      default:
        color = 0xFFFFFFFF;
        break;
    }

    var durSeconds = Conductor.instance.stepLengthMs * duration / 1000;

    if (duration <= 0) {
      return;
    }

    if (!applyToHud) {
      FlxG.camera.flash(color, durSeconds);
    } else {
      PlayState.instance.if (camHUD != null) camHUD.flash(color, durSeconds);
    }

  }

  @Override
    public void getTitle() {
    return eventTitle;
  }

  override private void getEventSchema(){
    return new ArrayList<Map<String, Object>>() { { add(new HashMap<String, Object>() { { put("name", "duration"); put("title", "Duration"); put("defaultValue", 1.0); put("step", 0.5); put("min", 0.5); put("type", "float"); put("units", "steps"); } }); add(new HashMap<String, Object>() { { put("name", "applyToHud"); put("title", "Apply to camHUD"); put("defaultValue", false); put("type", "bool"); } }); add(new HashMap<String, Object>() { { put("name", "color"); put("title", "Color"); put("defaultValue", 12); put("type", "enum"); put("keys", new HashMap<String, Object>() { { put("Black", 0); put("Blue", 1); put("Brown", 2); put("Cyan", 3); put("Gray", 4); put("Green", 5); put("Lime", 6); put("Magenta", 7); put("Orange", 8); put("Purple", 9); put("Red", 10); put("Transparent", 11); put("White", 12); put("Yellow", 13); } }); } }); } };
  }
}