package states;

import flixel.FlxState;
import flixel.FlxSprite;
import flixel.FlxG;
import flxanimate.FlxAnimatedSprite;

#if mobile
import flixel.input.touch.FlxTouch;
#end

class TitleState extends FlxState
{
	var logo:FlxAnimatedSprite;
	var gfDanceTitle:FlxAnimatedSprite;
	var titleEnter:FlxAnimatedSprite;
	var gfDanceTitlebg:FlxSprite;

	var pressedEnter:Bool = false;

	override public function create():Void
	{
		super.create();

		gfDanceTitlebg = new FlxSprite(0, 0, "assets/images/menus/titlescreen/gfDanceTitlebg.png");
		add(gfDanceTitlebg);

		gfDanceTitle = new FlxAnimatedSprite(0, 0);
		gfDanceTitle.loadGraphic("assets/images/menus/titlescreen/gfDance.png", FlxAnimatedSprite.sparrowAtlas, 1, true, 0, 0);
		gfDanceTitle.addAnimation("gfDance", [0, 1, 2, 3, 4, 5], 12, true);
		gfDanceTitle.play("gfDance");
		add(gfDanceTitle);

		logo = new FlxAnimatedSprite(0, 0);
		logo.loadGraphic("assets/images/menus/titlescreen/logo.png", FlxAnimatedSprite.sparrowAtlas, 1, true, 0, 0);
		logo.addAnimation("logo bumpin", [0, 1, 2, 3, 4, 5], 12, true);
		logo.play("logo bumpin");
		add(logo);

		titleEnter = new FlxAnimatedSprite(0, 0);
		titleEnter.loadGraphic("assets/images/menus/titlescreen/titleEnter.png", FlxAnimatedSprite.sparrowAtlas, 1, true, 0, 0);
		titleEnter.addAnimation("ENTER PRESSED", [0, 1, 2, 3, 4, 5], 12, true);
		titleEnter.addAnimation("ENTER FREEZE", [0], 12, true);
		titleEnter.play("ENTER FREEZE");
		add(titleEnter);
	}

	override public function update(elapsed:Float):Void
	{
		super.update(elapsed);

		if (!pressedEnter && checkEnterInput())
		{
			pressedEnter = true;
			titleEnter.play("ENTER PRESSED");
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
