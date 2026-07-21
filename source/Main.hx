
package;

import openfl.display.Sprite;
import openfl.display.StageAlign;
import openfl.display.StageScaleMode;
import openfl.events.Event;
import flixel.FlxGame;

import states.TitleState;
import mobile.backend.StorageUtil;

#if android
import lime.system.System as LimeSystem;
#end

class Main extends Sprite
{
	public static var storageInitialized:Bool = false;

	public function new()
	{
		super();

		if (stage != null)
		{
			init();
		}
		else
		{
			addEventListener(Event.ADDED_TO_STAGE, onAddedToStage);
		}
	}

	private function onAddedToStage(e:Event):Void
	{
		removeEventListener(Event.ADDED_TO_STAGE, onAddedToStage);
		init();
	}

	private function init():Void
	{
		if (stage != null)
		{
			stage.align = StageAlign.TOP_LEFT;
			stage.scaleMode = StageScaleMode.NO_SCALE;
		}

		setupStorage();
		setupGame();
	}

	private function setupStorage():Void
	{
		#if android
		StorageUtil.requestExternalStoragePermission();
		#end

		StorageUtil.ensureDirectory("mods");
		StorageUtil.ensureDirectory("saves");
		StorageUtil.ensureDirectory("logs");
		StorageUtil.ensureDirectory("cache");

		storageInitialized = true;
	}

	private function setupGame():Void
	{
		addChild(new FlxGame(0, 0, TitleState, 60, 60, true));
	}
}
