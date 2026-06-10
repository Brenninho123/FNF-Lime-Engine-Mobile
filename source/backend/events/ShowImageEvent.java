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

public class ShowImageEvent extends ScriptedSongEvent {
    public ShowImageEvent() {
        super("ShowImageEvent");
    }

    @Override
    public void handleEvent(Dynamic data) void {
        var playState = PlayState.instance;

        var String imageName = if (data.getString("image") != null) data.getString("image") else "GGMU";
        var Float stayTime = if (data.getFloat("stay") != null) data.getFloat("stay") else 0.6;
        var Bool fadeInEnabled = if (data.getBool("fadeIn") != null) data.getBool("fadeIn") else true;
        var Bool fadeOutEnabled = if (data.getBool("fadeOut") != null) data.getBool("fadeOut") else true;
        var Float fadeInTime = if (data.getFloat("fadeInTime") != null) data.getFloat("fadeInTime") else 0.4;
        var Float fadeOutTime = if (data.getFloat("fadeOutTime") != null) data.getFloat("fadeOutTime") else 0.4;
        var Bool shake = if (data.getBool("shake") != null) data.getBool("shake") else false;
        var Float shakeStrength = if (data.getFloat("shakeStrength") != null) data.getFloat("shakeStrength") else 5;
        var Float shakeSpeed = if (data.getFloat("shakeSpeed") != null) data.getFloat("shakeSpeed") else 0.05;
        var Float scaleVal = if (data.getFloat("scale") != null) data.getFloat("scale") else 1;
        var String camType = if (data.getString("cam") != null) data.getString("cam") else "hud";
        var Float posX = if (data.getFloat("x") != null) data.getFloat("x") else 0;
        var Float posY = if (data.getFloat("y") != null) data.getFloat("y") else 0;

        var img = new FlxSprite().loadGraphic(Paths.image("Showimage/" + imageName));
        img.scrollFactor.set(0,0);
        img.x = posX;
        img.y = posY;
        img.scale.set(scaleVal, scaleVal);

        switch (camType.toLowerCase()) {
            case "game":
        img.cameras = [playState.camGame];
        break;
            case "other":
        img.cameras = [playState.camOther];
        break;
            default:
        img.cameras = [playState.camHUD];
        break;
        }

        playState.add(img);

        // fade in
        if (fadeInEnabled) img.alpha = 0;
        FlxTween.tween(img, new Object() { public double alpha = fadeInEnabled ? 1 : img.alpha; }, fadeInTime);

        if (shake) {
            var baseX = img.x;
            FlxTween.tween(img, {x: baseX + shakeStrength}, shakeSpeed, {
                onComplete: function(_) {
                    FlxTween.tween(img, {x: baseX - shakeStrength}, shakeSpeed, {
                        onComplete: function(_) {
                            FlxTween.tween(img, {x: baseX}, shakeSpeed);
                        }
                    });
                }
            });
        }

        new FlxTimer().start(stayTime, function(_) {
            if (fadeOutEnabled) {
                FlxTween.tween(img, new Object() { public double alpha = 0; }, fadeOutTime, {onComplete: function(_) img.destroy()});
            } else {
                img.destroy();
            }
        });
    }

    @Override
    public void getTitle() String {
        return "Show Image Event";
    }

    @Override
    public void getIconPath() String {
        return "ui/chart-editor/events/showImage";
    }

    @Override
    public void getEventSchema() Object {
        return new ArrayList<Map<String, Object>>() { { add(new HashMap<String, Object>() { { put("name", "image"); put("title", "Image Name"); put("defaultValue", "GGMU"); put("type", "string"); } }); add(new HashMap<String, Object>() { { put("name", "stay"); put("title", "Stay Time"); put("defaultValue", 0.6); put("type", "float"); } }); add(new HashMap<String, Object>() { { put("name", "fadeIn"); put("title", "Enable Fade In"); put("defaultValue", true); put("type", "bool"); } }); add(new HashMap<String, Object>() { { put("name", "fadeOut"); put("title", "Enable Fade Out"); put("defaultValue", true); put("type", "bool"); } }); add(new HashMap<String, Object>() { { put("name", "fadeInTime"); put("title", "Fade In Time"); put("defaultValue", 0.4); put("type", "float"); } }); add(new HashMap<String, Object>() { { put("name", "fadeOutTime"); put("title", "Fade Out Time"); put("defaultValue", 0.4); put("type", "float"); } }); add(new HashMap<String, Object>() { { put("name", "shake"); put("title", "Shake On Show"); put("defaultValue", false); put("type", "bool"); } }); add(new HashMap<String, Object>() { { put("name", "shakeStrength"); put("title", "Shake Strength"); put("defaultValue", 5); put("type", "float"); } }); add(new HashMap<String, Object>() { { put("name", "shakeSpeed"); put("title", "Shake Speed"); put("defaultValue", 0.05); put("type", "float"); } }); add(new HashMap<String, Object>() { { put("name", "scale"); put("title", "Scale"); put("defaultValue", 1); put("type", "float"); } }); add(new HashMap<String, Object>() { { put("name", "cam"); put("title", "Camera"); put("defaultValue", "hud"); put("type", "string"); } }); add(new HashMap<String, Object>() { { put("name", "x"); put("title", "X Position"); put("defaultValue", 0); put("type", "float"); } }); add(new HashMap<String, Object>() { { put("name", "y"); put("title", "Y Position"); put("defaultValue", 0); put("type", "float"); } }); } };
    }
}