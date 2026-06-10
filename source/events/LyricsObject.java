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

public class LyricsObject extends ScriptedModule {
	var FlxText subtitleText;
	var FlxText subtitleTextOVER;

	var FlxTween fadeTween;

	var Float textAlpha = 1;

	var Bool addedStuff = false;

	public LyricsObject() {
		super("LyricsObject");
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

			subtitleTextOVER = new FlxText(0, 540, FlxG.width, "");
			subtitleTextOVER.setFormat(Paths.font("vcr.ttf"), 32, 0xFFFFFFFF, "center", FlxTextBorderStyle.OUTLINE, 0xFF000000);
			subtitleTextOVER.borderSize = 1.25;
			subtitleTextOVER.alpha = 1;
			subtitleTextOVER.zIndex = 3000;
			subtitleTextOVER.cameras = [PlayState.instance.camCutscene];
			PlayState.instance.add(subtitleTextOVER);

			subtitleText = new FlxText(0, 540, FlxG.width, "");
			subtitleText.setFormat(Paths.font("vcr.ttf"), 32, 0xFFFFFFFF, "center", FlxTextBorderStyle.OUTLINE, 0xFF000000);
			subtitleText.borderSize = 1.25;
			subtitleText.alpha = 1;
			subtitleText.zIndex = 850;
			subtitleText.cameras = [PlayState.instance.camHUD];
			PlayState.instance.add(subtitleText);
		}
	}

	private void onSongRetry(ScriptEvent event) {
		super.onSongRetry(event);

		subtitleText.text = "";
		subtitleTextOVER.text = "";

		subtitleText.alpha = textAlpha;
		subtitleTextOVER.alpha = textAlpha;
	}

	private void onGameOver(ScriptEvent event) void {
		super.onGameOver(event);

		subtitleText.text = "";
		subtitleTextOVER.text = "";

		subtitleText.alpha = textAlpha;
		subtitleTextOVER.alpha = textAlpha;
	}

	@Override
    public void onSongEvent(SongEventScriptEvent scriptEvent) {
		super.onSongEvent(scriptEvent);

		if (scriptEvent.eventData.eventKind == "Lyrics") {
			if (scriptEvent.eventData.value.lyric != null) {
				subtitleText.text = scriptEvent.eventData.value.lyric;
				subtitleTextOVER.text = scriptEvent.eventData.value.lyric;
			}

			if (scriptEvent.eventData.value.font != null) {
				subtitleText.font = Paths.font(scriptEvent.eventData.value.font + ".ttf");
				subtitleTextOVER.font = Paths.font(scriptEvent.eventData.value.font + ".ttf");
			}

			if (scriptEvent.eventData.value.fontSize != null) {
				subtitleText.size = scriptEvent.eventData.value.fontSize;
				subtitleTextOVER.size = scriptEvent.eventData.value.fontSize;
			}

			if (scriptEvent.eventData.value.letterSpacing != null) {
				subtitleText.letterSpacing = scriptEvent.eventData.value.letterSpacing;
				subtitleTextOVER.letterSpacing = scriptEvent.eventData.value.letterSpacing;
			}

			if (scriptEvent.eventData.value.textColor != null) {
				if (scriptEvent.eventData.value.textColor == 0) {
					subtitleText.color = 0xFFFFFFFF;
					subtitleTextOVER.color = 0xFFFFFFFF;
				} else if (scriptEvent.eventData.value.textColor == 1) {
					subtitleText.color = 0xFF000000;
					subtitleTextOVER.color = 0xFF000000;
				} else if (scriptEvent.eventData.value.textColor == 2) {
					subtitleText.color = 0xFF0000FF;
					subtitleTextOVER.color = 0xFF0000FF;
				} else if (scriptEvent.eventData.value.textColor == 3) {
					subtitleText.color = 0xFF8B4513;
					subtitleTextOVER.color = 0xFF8B4513;
				} else if (scriptEvent.eventData.value.textColor == 4) {
					subtitleText.color = 0xFF00FFFF;
					subtitleTextOVER.color = 0xFF00FFFF;
				} else if (scriptEvent.eventData.value.textColor == 5) {
					subtitleText.color = 0xFF808080;
					subtitleTextOVER.color = 0xFF808080;
				} else if (scriptEvent.eventData.value.textColor == 6) {
					subtitleText.color = 0xFF008000;
					subtitleTextOVER.color = 0xFF008000;
				} else if (scriptEvent.eventData.value.textColor == 7) {
					subtitleText.color = 0xFF00FF00;
					subtitleTextOVER.color = 0xFF00FF00;
				} else if (scriptEvent.eventData.value.textColor == 8) {
					subtitleText.color = 0xFFFF00FF;
					subtitleTextOVER.color = 0xFFFF00FF;
				} else if (scriptEvent.eventData.value.textColor == 9) {
					subtitleText.color = 0xFFFFA500;
					subtitleTextOVER.color = 0xFFFFA500;
				} else if (scriptEvent.eventData.value.textColor == 10) {
					subtitleText.color = 0xFFFFC0CB;
					subtitleTextOVER.color = 0xFFFFC0CB;
				} else if (scriptEvent.eventData.value.textColor == 11) {
					subtitleText.color = 0xFF800080;
					subtitleTextOVER.color = 0xFF800080;
				} else if (scriptEvent.eventData.value.textColor == 12) {
					subtitleText.color = 0xFFFF0000;
					subtitleTextOVER.color = 0xFFFF0000;
				} else if (scriptEvent.eventData.value.textColor == 13) {
					subtitleText.color = 0xFFFFFF00;
					subtitleTextOVER.color = 0xFFFFFF00;
				}
			}

			if (scriptEvent.eventData.value.alignment != null) {
				if (scriptEvent.eventData.value.alignment == 0) {
					subtitleText.alignment = "center";
					subtitleTextOVER.alignment = "center";
				} else if (scriptEvent.eventData.value.alignment == 1) {
					subtitleText.alignment = "left";
					subtitleTextOVER.alignment = "left";
				} else if (scriptEvent.eventData.value.alignment == 2) {
					subtitleText.alignment = "right";
					subtitleTextOVER.alignment = "right";
				} else if (scriptEvent.eventData.value.alignment == 3) {
					subtitleText.alignment = "justify";
					subtitleTextOVER.alignment = "justify";
				}
			}

			if (scriptEvent.eventData.value.position != null) {
				if (scriptEvent.eventData.value.position == 0) {
					if (PlayState.instance.healthBar.y < 360) // downscroll
					{
						subtitleText.x = 0;
						subtitleText.y = 130;
						subtitleTextOVER.x = 0;
						subtitleTextOVER.y = 130;
					} else {
						subtitleText.x = 0;
						subtitleText.y = 540;
						subtitleTextOVER.x = 0;
						subtitleTextOVER.y = 540;
					}
				} else if (scriptEvent.eventData.value.position == 1) {
					if (PlayState.instance.healthBar.y < 360) // downscroll
					{
						subtitleText.x = 0;
						subtitleText.y = 540;
						subtitleTextOVER.x = 0;
						subtitleTextOVER.y = 540;
					} else {
						subtitleText.x = 0;
						subtitleText.y = 130;
						subtitleTextOVER.x = 0;
						subtitleTextOVER.y = 130;
					}
				} else if (scriptEvent.eventData.value.position == 2) {
					subtitleText.screenCenter();
					subtitleTextOVER.screenCenter();
				} else if (scriptEvent.eventData.value.position == 3) {
					if (scriptEvent.eventData.value.customX != null) {
						subtitleText.x = scriptEvent.eventData.value.customX;
						subtitleTextOVER.x = scriptEvent.eventData.value.customX;
					}

					if (scriptEvent.eventData.value.customY != null) {
						subtitleText.y = scriptEvent.eventData.value.customY;
						subtitleTextOVER.y = scriptEvent.eventData.value.customY;
					}
				}
			} else {
				if (PlayState.instance.healthBar.y < 360) // downscroll
				{
					subtitleText.x = 0;
					subtitleText.y = 130;
					subtitleTextOVER.x = 0;
					subtitleTextOVER.y = 130;
				} else {
					subtitleText.x = 0;
					subtitleText.y = 540;
					subtitleTextOVER.x = 0;
					subtitleTextOVER.y = 540;
				}
			}

			if (scriptEvent.eventData.value.outlineWidth != null) {
				if (scriptEvent.eventData.value.outlineWidth == 1.3) {
					subtitleText.borderSize = 1.25;
					subtitleTextOVER.borderSize = 1.25;
				} else {
					subtitleText.borderSize = scriptEvent.eventData.value.outlineWidth;
					subtitleTextOVER.borderSize = scriptEvent.eventData.value.outlineWidth;
				}
			}

			/*
				if (scriptEvent.eventData.value.borderColor != null)
				{
					if (scriptEvent.eventData.value.borderColor == 0)
					{
						subtitleText.borderColor = 0xFFFFFFFF;
						subtitleTextOVER.borderColor = 0xFFFFFFFF;
					}
					else if (scriptEvent.eventData.value.borderColor == 1)
					{
						subtitleText.borderColor = 0xFF000000;
						subtitleTextOVER.borderColor = 0xFF000000;
					}
					else if (scriptEvent.eventData.value.borderColor == 2)
					{
						subtitleText.borderColor = 0xFF0000FF;
						subtitleTextOVER.borderColor = 0xFF0000FF;
					}
					else if (scriptEvent.eventData.value.borderColor == 3)
					{
						subtitleText.borderColor = 0xFF8B4513;
						subtitleTextOVER.borderColor = 0xFF8B4513;
					}
					else if (scriptEvent.eventData.value.borderColor == 4)
					{
						subtitleText.borderColor = 0xFF00FFFF;
						subtitleTextOVER.borderColor = 0xFF00FFFF;
					}
					else if (scriptEvent.eventData.value.borderColor == 5)
					{
						subtitleText.borderColor = 0xFF808080;
						subtitleTextOVER.borderColor = 0xFF808080;
					}
					else if (scriptEvent.eventData.value.borderColor == 6)
					{
						subtitleText.borderColor = 0xFF008000;
						subtitleTextOVER.borderColor = 0xFF008000;
					}
					else if (scriptEvent.eventData.value.borderColor == 7)
					{
						subtitleText.borderColor = 0xFF00FF00;
						subtitleTextOVER.borderColor = 0xFF00FF00;
					}
					else if (scriptEvent.eventData.value.borderColor == 8)
					{
						subtitleText.borderColor = 0xFFFF00FF;
						subtitleTextOVER.borderColor = 0xFFFF00FF;
					}
					else if (scriptEvent.eventData.value.borderColor == 9)
					{
						subtitleText.borderColor = 0xFFFFA500;
						subtitleTextOVER.borderColor = 0xFFFFA500;
					}
					else if (scriptEvent.eventData.value.borderColor == 10)
					{
						subtitleText.borderColor = 0xFFFFC0CB;
						subtitleTextOVER.borderColor = 0xFFFFC0CB;
					}
					else if (scriptEvent.eventData.value.borderColor == 11)
					{
						subtitleText.borderColor = 0xFF800080;
						subtitleTextOVER.borderColor = 0xFF800080;
					}
					else if (scriptEvent.eventData.value.borderColor == 12)
					{
						subtitleText.borderColor = 0xFFFF0000;
						subtitleTextOVER.borderColor = 0xFFFF0000;
					}
					else if (scriptEvent.eventData.value.borderColor == 13)
					{
						subtitleText.borderColor = 0xFFFFFF00;
						subtitleTextOVER.borderColor = 0xFFFFFF00;
					}
				}
			 */

			if (scriptEvent.eventData.value.opacity != null) {
				subtitleText.alpha = scriptEvent.eventData.value.opacity / 100;
				subtitleTextOVER.alpha = scriptEvent.eventData.value.opacity / 100;

				textAlpha = scriptEvent.eventData.value.opacity / 100;
			}

			if (scriptEvent.eventData.value.layering != null) {
				if (scriptEvent.eventData.value.layering == 1) {
					subtitleText.visible = false;
					subtitleTextOVER.visible = true;
				} else {
					subtitleText.visible = true;
					subtitleTextOVER.visible = false;
				}
			} else {
				subtitleText.visible = true;
				subtitleTextOVER.visible = false;
			}
		}

		if (scriptEvent.eventData.eventKind == "FadeInOut") {
			if (scriptEvent.eventData.value.object == 2) {
				if (scriptEvent.eventData.value.inout == 1) {
					if (scriptEvent.eventData.value.seconds != 0) {
						fadeTween = FlxTween.tween(subtitleText, new Object() { public double alpha = 0; }, scriptEvent.eventData.value.seconds);
						fadeTween = FlxTween.tween(subtitleTextOVER, new Object() { public double alpha = 0; }, scriptEvent.eventData.value.seconds);
					} else {
						subtitleText.alpha = 0;
						subtitleTextOVER.alpha = 0;
					}
				} else {
					if (scriptEvent.eventData.value.seconds != 0) {
						fadeTween = FlxTween.tween(subtitleText, new Object() { public double alpha = textAlpha; }, scriptEvent.eventData.value.seconds);
						fadeTween = FlxTween.tween(subtitleTextOVER, new Object() { public double alpha = textAlpha; }, scriptEvent.eventData.value.seconds);
					} else {
						subtitleText.alpha = textAlpha;
						subtitleTextOVER.alpha = textAlpha;
					}
				}
			}
		}
	}
}