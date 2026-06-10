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

public class CameraShakeFixes extends ScriptedModule {
  var Bool shouldFixShakeEvent = true;

  public cameraShakeEvent(){
    super("cameraShakeFixes");
  }
  
  private void onGameOver(ScriptEvent event){
    super.onGameOver(event);
    if (PlayState.instance.camGame != null && shouldFixShakeEvent) PlayState.instance.camGame.stopShake();
    if (PlayState.instance.camHUD != null && shouldFixShakeEvent) PlayState.instance.camHUD.stopShake();
  }

  private void onSongRetry(ScriptEvent event){
    super.onSongRetry(event);
    if (PlayState.instance.camGame != null && shouldFixShakeEvent) PlayState.instance.camGame.stopShake();
    if (PlayState.instance.camHUD != null && shouldFixShakeEvent) PlayState.instance.camHUD.stopShake();
  }
}

public class CameraShakeEvent extends ScriptedSongEvent {
  private void new() {
    super("cameraShakeEvent");
  }

  public String eventTitle = "Camera Shake";
  public var isEnabled = null;

  // best if we could change both without placing two separate events
  public Float DEFAULT_CAMGAME_INTENSITY = 0.02;
  public Float DEFAULT_CAMHUD_INTENSITY = 0.01;
  public Float DEFAULT_DURATION = 1.0;
  public Int DEFAULT_DIRECTION = 2;

  // public FlxTween tween = null; // Can"t figure out how to do easing on shaking effects

  override private void handleEvent(data) void {
    if (PlayState.instance == null || PlayState.instance.currentStage == null) return;
    if (PlayState.instance.isMinimalMode) return;

    // Save Stuff
    isEnabled = Save.instance.modOptions.get("extra-events").isShakeEnabled;
    if (!isEnabled) return; // Check if the option is enabled/disabled every time the event is called

    var Float intensity_CamGame = data.get("intensityCamGame") != null ? ((Number)data.get("intensityCamGame")).floatValue() : DEFAULT_CAMGAME_INTENSITY;
    var Float intensity_CamHUD = data.get("intensityCamHUD") != null ? ((Number)data.get("intensityCamHUD")).floatValue() : DEFAULT_CAMHUD_INTENSITY;
    var Float duration = data.get("duration") != null ? ((Number)data.get("duration")).floatValue() : DEFAULT_DURATION;
    var Int direction = data.get("direction") != null ? ((Number)data.get("direction")).intValue() : DEFAULT_DIRECTION;

    var durSeconds = Conductor.instance.stepLengthMs * duration / 1000;

    switch (direction) {
      case 0: // X
        direction = 0x01;
      case 1: // Y
        direction = 0x10;
      case 2: // Both
        direction = 0x11;

      default:
        direction = 0x11;
        break;
    }

    if (duration <= 0) {
      return;
    }

    if (intensity_CamGame != 0) FlxG.camera.shake(intensity_CamGame, durSeconds, direction);
    if (intensity_CamHUD != 0) PlayState.instance.if (camHUD != null) camHUD.shake(intensity_CamHUD, durSeconds, direction);
  }

  @Override
    public void getTitle() {
    return eventTitle;
  }

  @Override
    public void getIconPath(){
    return "ui/chart-editor/events/cameraShakeEvent";
  }

  override private void getEventSchema(){
    return new ArrayList<Map<String, Object>>() { { add(new HashMap<String, Object>() { { put("name", "intensityCamGame"); put("title", "CamGame Intensity"); put("defaultValue", 0.02); put("step", 0.01); put("min", 0); put("type", "float"); put("units", "x"); } }); add(new HashMap<String, Object>() { { put("name", "intensityCamHUD"); put("title", "CamHUD Intensity"); put("defaultValue", 0.01); put("step", 0.01); put("min", 0); put("type", "float"); put("units", "x"); } }); add(new HashMap<String, Object>() { { put("name", "duration"); put("title", "Duration"); put("defaultValue", 1.0); put("step", 0.5); put("min", 0.5); put("type", "float"); put("units", "steps"); } }); add(new HashMap<String, Object>() { { put("name", "direction"); put("title", "Direction',
        defaultValue: 2,
        type: "); put("keys", new HashMap<String, Object>() { { put("Horizontal", 0); put("Vertical", 1); put("Both", 2); } }); } }); } };
  }
}