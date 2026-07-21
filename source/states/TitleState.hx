package states;

import flixel.FlxState;
import flixel.FlxSprite;
import flixel.FlxG;
import flixel.graphics.frames.FlxAtlasFrames;
import flixel.util.FlxColor;

#if mobile
import flixel.input.touch.FlxTouch;
#end

class TitleState extends FlxState
{
	var logo:FlxSprite;
	var gfDanceTitle:FlxSprite;
	var titleEnter:FlxSprite;
	var gfDanceTitlebg:FlxSprite;

	var pressedEnter:Bool = false;

	override public function create():Void
	{
		super.create();

		gfDanceTitlebg = new FlxSprite();
		gfDanceTitlebg.loadGraphic("assets/images/menus/titlescreen/gfDanceTitlebg.png");
		add(gfDanceTitlebg);

		gfDanceTitle = new FlxSprite();
		gfDanceTitle.frames = FlxAtlasFrames.fromSparrow("assets/images/menus/titlescreen/gfDance.png", "assets/images/menus/titlescreen/gfDance.xml");
		gfDanceTitle.animation.addByPrefix("gfDance", "gfDance", 12, true);
		gfDanceTitle.animation.play("gfDance");
		add(gfDanceTitle);

		logo = new FlxSprite();
		logo.frames = FlxAtlasFrames.fromSparrow("assets/images/menus/titlescreen/logo.png", "assets/images/menus/titlescreen/logo.xml");
		logo.animation.addByPrefix("logo bumpin", "logo bumpin", 12, true);
		logo.animation.play("logo bumpin");
		add(logo);

		titleEnter = new FlxSprite();
		titleEnter.frames = FlxAtlasFrames.fromSparrow("assets/images/menus/titlescreen/titleEnter.png", "assets/images/menus/titlescreen/titleEnter.xml");
		titleEnter.animation.addByPrefix("ENTER PRESSED", "ENTER PRESSED", 12, true);
		titleEnter.animation.addByPrefix("ENTER FREEZE", "ENTER FREEZE", 12, true);
		titleEnter.animation.play("ENTER FREEZE");
		add(titleEnter);
	}

	override public function update(elapsed:Float):Void
	{
		super.update(elapsed);

		if (!pressedEnter && checkEnterInput())
		{
			pressedEnter = true;
			titleEnter.animation.play("ENTER PRESSED");
			onEnterPressed();
		}
	}

	function checkEnterInput():Bool
	{
		#if mobile
		for (touch in FlxG.touches.list)
		{
			if (touch.justPressed)
				return true;
		}
		#end

		#if !mobile
		if (FlxG.keys.justPressed.ENTER)
			return true;

		if (FlxG.mouse.justPressed)
			return true;
		#end

		return false;
	}

	function onEnterPressed():Void
	{
	}
}
