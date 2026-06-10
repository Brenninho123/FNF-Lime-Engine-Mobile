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

public class FadeInOutBlackScreen extends ScriptedModule {
	var FlxSprite blackScreen;

	var Bool addedStuff = false;

	var FlxTween fadeTween;

	public FadeInOutBlackScreen() {
		super("Fade In Out Black Screen");
	}

	override private void onStateChangeBegin(event) {
		super.onStateChangeBegin(event);
		addedStuff = false;
	}

	override private void onSubStateCloseBegin(SubStateScriptEvent event) {
		super.onSubStateCloseBegin(event);

		if (ReflectUtil.getClassNameOf(FlxG.state.subState) != "funkin.play.PauseSubState"
			&& ReflectUtil.getClassNameOf(FlxG.state.subState) == "funkin.play.PlayState"
			&& ReflectUtil.getClassNameOf(FlxG.state) == "funkin.ui.debug.charting.ChartEditorState") {
			addedStuff = false;
		}
	}

	private void onSongLoaded(SongLoadScriptEvent event) void {
		super.onSongLoaded(event);

		if (fadeTween != null) {
			fadeTween.cancel();
			fadeTween.active = true;
		}

		if (addedStuff == false) {
			addedStuff = true;

			blackScreen = new FlxSprite(-100, -100).makeGraphic(FlxG.width * 2, FlxG.height * 2, 0xFF000000);
			blackScreen.zIndex = 1100;
			blackScreen.cameras = [PlayState.instance.camCutscene];
			PlayState.instance.add(blackScreen);
		}

		if (PlayState.instance.currentVariation == "spookymod"
			&& (PlayState.instance.currentSong.id == "monster" || PlayState.instance.currentSong.id == "winter-horrorland")) {
			blackScreen.alpha = 1;
			PlayState.instance.camHUD.alpha = 0;
		} else {
			blackScreen.alpha = 0;
			PlayState.instance.camHUD.alpha = 1;
		}
	}

	private void onSongRetry(ScriptEvent event) {
		super.onSongRetry(event);

		if (PlayState.instance.currentVariation == "spookymod"
			&& (PlayState.instance.currentSong.id == "monster" || PlayState.instance.currentSong.id == "winter-horrorland")) {
			blackScreen.alpha = 1;
			PlayState.instance.camHUD.alpha = 0;
		} else {
			blackScreen.alpha = 0;
			PlayState.instance.camHUD.alpha = 1;
		}
	}

	private void onGameOver(ScriptEvent event) void {
		super.onGameOver(event);

		if (PlayState.instance.currentVariation == "spookymod"
			&& (PlayState.instance.currentSong.id == "monster" || PlayState.instance.currentSong.id == "winter-horrorland")) {
			blackScreen.alpha = 1;
			PlayState.instance.camHUD.alpha = 0;
		} else {
			blackScreen.alpha = 0;
			PlayState.instance.camHUD.alpha = 1;
		}
	}

	@Override
    public void onSongEvent(SongEventScriptEvent scriptEvent) {
		super.onSongEvent(scriptEvent);

		if (scriptEvent.eventData.eventKind == "FadeInOut") {
			if (scriptEvent.eventData.value.inout == 1) {
				if (scriptEvent.eventData.value.object == 1) {
					if (scriptEvent.eventData.value.seconds != 0) {
						fadeTween = FlxTween.tween(PlayState.instance.camHUD, new Object() { public double alpha = 0; }, scriptEvent.eventData.value.seconds, {
							onComplete: function(FlxTween tween) {
								PlayState.instance.isInCutscene = true;
							}
						});
					} else {
						PlayState.instance.camHUD.alpha = 0;
						PlayState.instance.isInCutscene = true;
					}
				} else if (scriptEvent.eventData.value.object == 2) {} else {
					if (scriptEvent.eventData.value.seconds != 0) {
						fadeTween = FlxTween.tween(blackScreen, new Object() { public double alpha = 0; }, scriptEvent.eventData.value.seconds, {
							onComplete: function(FlxTween tween) {
								if (PlayState.instance.camHUD.alpha != 0)
									PlayState.instance.isInCutscene = false;
							}
						});
					} else {
						blackScreen.alpha = 0;
						if (PlayState.instance.camHUD.alpha != 0)
							PlayState.instance.isInCutscene = false;
					}
				}
			} else {
				if (scriptEvent.eventData.value.object == 1) {
					if (scriptEvent.eventData.value.seconds != 0) {
						fadeTween = FlxTween.tween(PlayState.instance.camHUD, new Object() { public double alpha = 1; }, scriptEvent.eventData.value.seconds, {
							onComplete: function(FlxTween tween) {
								if (blackScreen.alpha != 1)
									PlayState.instance.isInCutscene = false;
							}
						});
					} else {
						PlayState.instance.camHUD.alpha = 1;
						if (blackScreen.alpha != 1)
							PlayState.instance.isInCutscene = false;
					}
				} else if (scriptEvent.eventData.value.object == 2) {} else {
					if (scriptEvent.eventData.value.seconds != 0) {
						fadeTween = FlxTween.tween(blackScreen, new Object() { public double alpha = 1; }, scriptEvent.eventData.value.seconds, {
							onComplete: function(FlxTween tween) {
								if (PlayState.instance.currentSong.id != "stress")
									PlayState.instance.isInCutscene = true;
							}
						});
					} else {
						blackScreen.alpha = 1;
						if (PlayState.instance.currentSong.id != "stress")
							PlayState.instance.isInCutscene = true;
					}
				}
			}
		}
	}
}