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

/**
 * This is a Song Event designed for .mp4 video playback.
 * Parameters are passed through VideoModule and then the video is played.
 *
 * ~ EXAMPLE ~
 *
 * ```
 * {
 *   "e": "PlayVideo",
 *   "v": {
 *      "path": "ughCutscene",
 *      "isCutscene": 1,
 *      "duration": 1,
 *      "mute": false,
 *      "disableControl": false,
 *      "resync": true,
 *      "zIndex": 300
 *   }
 * }
 * ```
 */
public class PlayVideoEvent extends ScriptedSongEvent
{
  private void new()
  {
    super("PlayVideo");
  }

  // Pass everything to VideoModule and play the specified video.
  override private void handleEvent(data)
  {
    if (PlayState.instance == null) return;
    ModuleHandler.getModule("PVE_VideoModule").scriptCall("createVideo", [
      Paths.videos(data.value.path),
      {
        videoType: data.value.isCutscene,
        disableControls: data.value.disableControl,
        hudFadeDuration: data.value.duration,
        resync: data.value.resync,
        zIndex: data.value.zIndex,
        mute: data.value.mute,
        timestamp: data.time
      }
    ]);
  }

  override private void getTitle()
  {
    return "Play Video";
  }

  /**
   * The actual parameters themselves, they should be self explanatory.
   * It"s worth noting, however, that "HUD fade duration" won"t actually do anything
   * if you don"t hide the HUD or choose Instant in the Chart Editor.
   */
  override private void getEventSchema()
  {
    return new ArrayList<Map<String, Object>>() { { add(new HashMap<String, Object>() { { put("name", "path"); put("title", "Video file name"); put("defaultValue", "File name (no .mp4)"); put("type", "string"); } }); add(new HashMap<String, Object>() { { put("name", "isCutscene"); put("title", "Hide HUD?"); put("defaultValue", "No"); put("type", "enum"); put("keys", new HashMap<String, Object>() { { put("No", 1); put("Yes (Fade the hud)", 2); put("Yes (No Fade, ignores "HUD fade duration")", 3); } }); } }); add(new HashMap<String, Object>() { { put("name", "duration"); put("title", "HUD fade duration',
        defaultValue: 1,
        min: 0,
        type: "); } }); add(new HashMap<String, Object>() { { put("name", "mute"); put("title", "Mute?"); put("defaultValue", false); put("type", "bool"); } }); add(new HashMap<String, Object>() { { put("name", "disableControl"); put("title", "Disable controls?"); put("defaultValue", false); put("type", "bool"); } }); add(new HashMap<String, Object>() { { put("name", "resync"); put("title", "Resync automatically?"); put("defaultValue", true); put("type", "bool"); } }); add(new HashMap<String, Object>() { { put("name", "zIndex"); put("title", "zIndex"); put("defaultValue", 300); put("type", "float"); } }); } };
  }
}