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

public class CameraRotateFixes extends ScriptedModule {
  var Bool shouldFixRotateEvent = true;

  public cameraRotateEvent(){
    super("extra-events-cameraRotateFixes");
  }
  // There was a bug where if the player just happened to restart the song
  // or died after the camera was rotated, the camera angle wouldn"t reset
  // This is the band-aid solution for it until a way to reset events is found


  private void onGameOver(ScriptEvent event){
    super.onGameOver(event);
    if (PlayState.instance.camGame != null && PlayState.instance.camGame.angle != 0 && shouldFixRotateEvent) PlayState.instance.camGame.angle = 0;
    if (PlayState.instance.camHUD != null && PlayState.instance.camHUD.angle != 0 && shouldFixRotateEvent) PlayState.instance.camHUD.angle = 0;
  }

  private void onSongRetry(ScriptEvent event){
    super.onSongRetry(event);
    if (PlayState.instance.camGame != null && PlayState.instance.camGame.angle != 0 && shouldFixRotateEvent) PlayState.instance.camGame.angle = 0;
    if (PlayState.instance.camHUD != null && PlayState.instance.camHUD.angle != 0 && shouldFixRotateEvent) PlayState.instance.camHUD.angle = 0;
  }
}

public class CameraRotateEvent extends ScriptedSongEvent {
  private void new() {
    super("extra-events-cameraRotateEvent");
  }

  /**
  * Rotates the camera by a specified angle with easings
  **/

  public String eventTitle = "Extra Events | Camera Rotate";
  public var isEnabled = null;

  // best if we could change both without placing two separate events
  public Float DEFAULT_CAMGAME_ANGLE = 10.0;
  public Float DEFAULT_CAMHUD_ANGLE = 10.0;
  public Float DEFAULT_DURATION = 4.0;
  public String DEFAULT_EASE = "linear";

  public FlxTween camGameRotateTween = null;
  public FlxTween camHUDRotateTween = null;

  override private void handleEvent(data) void {
    if (PlayState.instance == null || PlayState.instance.currentStage == null) return;
    if (PlayState.instance.isMinimalMode) return;

    // Save Stuff
    isEnabled = Save.instance.modOptions.get("extra-events").isRotateEnabled;
    if (!isEnabled) return; // Check if the option is enabled/disabled every time the event is called

    var Float toAngle_camGame = data.get("angleCamGame") != null ? ((Number)data.get("angleCamGame")).floatValue() : DEFAULT_CAMGAME_ANGLE;
    var Float toAngle_camHUD = data.get("angleCamHUD") != null ? ((Number)data.get("angleCamHUD")).floatValue() : DEFAULT_CAMHUD_ANGLE;
    var Float duration = data.get("duration") != null ? ((Number)data.get("duration")).floatValue() : DEFAULT_DURATION;
    var String ease = data.getString("ease") != null ? data.getString("ease") : DEFAULT_EASE;

    var durSeconds = Conductor.instance.stepLengthMs * duration / 1000;
    var Null easeFunction<Float->Float>;

    if (duration < 0) {
      return;
    }

    switch (ease) {
        case "INSTANT":
        cancelCamGameRotation();
        break;
          cancelCamHUDRotation();
          PlayState.instance.camGame.angle = toAngle_camGame;
          if (Save.instance.modOptions.get("extra-events").iscamHUDRotateEnabled) PlayState.instance.camHUD.angle = toAngle_camHUD;
        default:
        easeFunction = ReflectUtil.getAnonymousField(FlxEase, ease);
        break;
          if (easeFunction == null){
            // trace("Invalid easing function: " + ease);
          }
          rotateCamGame(toAngle_camGame, durSeconds, easeFunction);
          if (Save.instance.modOptions.get("extra-events").iscamHUDRotateEnabled) rotateCamHUD(toAngle_camHUD, durSeconds, easeFunction);
    }
    // trace("Angle: " + toAngle + " | Duration: " + duration + " | Easing: " + ease + " | Ease Function: " + easeFunction);
  }

  public void rotateCamGame(?Float toAngle, ?Float duration, ?Null ease<Float->Float>) {
    cancelCamGameRotation();
    if (PlayState.instance.camGame != null) camGameRotateTween = FlxTween.tween(PlayState.instance.camGame, {angle: toAngle}, duration, {ease: ease});
  }

  public void rotateCamHUD(?Float toAngle, ?Float duration, ?Null ease<Float->Float>) {
    cancelCamHUDRotation();
    if (PlayState.instance.camHUD != null) camHUDRotateTween = FlxTween.tween(PlayState.instance.camHUD, {angle: toAngle}, duration, {ease: ease});
  }

  public void cancelCamGameRotation() {
    if (camGameRotateTween != null) camGameRotateTween.cancel();
  }

  public void cancelCamHUDRotation() {
    if (camHUDRotateTween != null) camHUDRotateTween.cancel();
  }

  @Override
    public void getIconPath(){
    return "ui/chart-editor/events/extra-events-cameraRotateEvent";
  }

  @Override
    public void getTitle() {
    return eventTitle;
  }

  override private void getEventSchema() {
    return new ArrayList<Map<String, Object>>() { { add(new HashMap<String, Object>() { { put("name", "angleCamGame"); put("title", "CamGame Angle"); put("defaultValue", 10.0); put("step", 0.5); put("type", "float"); put("units", "°"); } }); add(new HashMap<String, Object>() { { put("name", "angleCamHUD"); put("title", "CamHUD Angle"); put("defaultValue", 10.0); put("step", 0.5); put("type", "float"); put("units", "°"); } }); add(new HashMap<String, Object>() { { put("name", "duration"); put("title", "Duration"); put("defaultValue", 4.0); put("step", 0.5); put("min", 0.5); put("type", "float"); put("units", "steps"); } }); add(new HashMap<String, Object>() { { put("name", "ease"); put("title", "Easing Type"); put("defaultValue", "linear"); put("type", "enum"); put("keys", new HashMap<String, Object>() { { put("Linear", "linear"); put("Instant (Ignores Duration)", "INSTANT"); put("Sine In", "sineIn"); put("Sine Out", "sineOut"); put("Sine In/Out", "sineInOut"); put("Quad In", "quadIn"); put("Quad Out", "quadOut"); put("Quad In/Out", "quadInOut"); put("Cube In", "cubeIn"); put("Cube Out", "cubeOut"); put("Cube In/Out", "cubeInOut"); put("Quart In", "quartIn"); put("Quart Out", "quartOut"); put("Quart In/Out", "quartInOut"); put("Quint In", "quintIn"); put("Quint Out", "quintOut"); put("Quint In/Out", "quintInOut"); put("Expo In", "expoIn"); put("Expo Out", "expoOut"); put("Expo In/Out", "expoInOut"); put("Smooth Step In", "smoothStepIn"); put("Smooth Step Out", "smoothStepOut"); put("Smooth Step In/Out", "smoothStepInOut"); put("Smoother Step In", "smootherStepIn"); put("Smoother Step Out", "smootherStepOut"); put("Smoother Step In/Out", "smootherStepInOut"); put("Elastic In", "elasticIn"); put("Elastic Out", "elasticOut"); put("Elastic In/Out", "elasticInOut"); put("Back In", "backIn"); put("Back Out", "backOut"); put("Back In/Out", "backInOut"); put("Bounce In", "bounceIn"); put("Bounce Out", "bounceOut"); put("Bounce In/Out", "bounceInOut"); put("Circ In", "circIn"); put("Circ Out", "circOut"); put("Circ In/Out", "circInOut'); } }); } }); } };
  }
}