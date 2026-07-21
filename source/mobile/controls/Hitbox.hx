package mobile.controls;

import flixel.FlxG;
import flixel.FlxSprite;
import flixel.group.FlxSpriteGroup;
import flixel.math.FlxRect;
import flixel.util.FlxColor;
import flixel.input.touch.FlxTouch;

enum HitboxDirection
{
	LEFT;
	DOWN;
	UP;
	RIGHT;
}

class HitboxButton extends FlxSprite
{
	public var direction:HitboxDirection;
	public var pressed:Bool = false;
	public var justPressed:Bool = false;
	public var justReleased:Bool = false;

	var wasPressed:Bool = false;
	var trackedTouchID:Int = -1;

	public function new(direction:HitboxDirection, x:Float, y:Float, width:Float, height:Float, color:FlxColor)
	{
		super(x, y);
		this.direction = direction;

		makeGraphic(Std.int(width), Std.int(height), color);
		alpha = 0.00001;
		scrollFactor.set();
		solid = false;
		immovable = true;
	}

	public function updateTouchState(touches:Array<FlxTouch>):Void
	{
		wasPressed = pressed;
		pressed = false;

		if (trackedTouchID != -1)
		{
			var stillDown:Bool = false;
			for (touch in touches)
			{
				if (touch.touchPointID == trackedTouchID && touch.pressed)
				{
					stillDown = true;
					pressed = true;
					break;
				}
			}

			if (!stillDown)
				trackedTouchID = -1;
		}

		if (trackedTouchID == -1)
		{
			for (touch in touches)
			{
				if (touch.justPressed && overlapsPoint(touch.getWorldPosition()))
				{
					trackedTouchID = touch.touchPointID;
					pressed = true;
					break;
				}
			}
		}

		justPressed = pressed && !wasPressed;
		justReleased = !pressed && wasPressed;
	}
}

class Hitbox extends FlxSpriteGroup
{
	public static var instance:Hitbox;

	var buttons:Map<HitboxDirection, HitboxButton> = new Map<HitboxDirection, HitboxButton>();

	public function new(?colorLeft:FlxColor, ?colorDown:FlxColor, ?colorUp:FlxColor, ?colorRight:FlxColor)
	{
		super();

		instance = this;

		var screenWidth:Float = FlxG.width;
		var screenHeight:Float = FlxG.height;
		var laneWidth:Float = screenWidth / 4;

		var left:HitboxButton = new HitboxButton(LEFT, laneWidth * 0, 0, laneWidth, screenHeight, colorLeft != null ? colorLeft : FlxColor.RED);
		var down:HitboxButton = new HitboxButton(DOWN, laneWidth * 1, 0, laneWidth, screenHeight, colorDown != null ? colorDown : FlxColor.BLUE);
		var up:HitboxButton = new HitboxButton(UP, laneWidth * 2, 0, laneWidth, screenHeight, colorUp != null ? colorUp : FlxColor.GREEN);
		var right:HitboxButton = new HitboxButton(RIGHT, laneWidth * 3, 0, laneWidth, screenHeight, colorRight != null ? colorRight : FlxColor.PURPLE);

		buttons.set(LEFT, left);
		buttons.set(DOWN, down);
		buttons.set(UP, up);
		buttons.set(RIGHT, right);

		add(left);
		add(down);
		add(up);
		add(right);

		scrollFactor.set();
	}

	override public function update(elapsed:Float):Void
	{
		super.update(elapsed);

		var touches:Array<FlxTouch> = FlxG.touches.list;

		for (button in buttons)
			button.updateTouchState(touches);
	}

	public function pressed(direction:HitboxDirection):Bool
	{
		var button:HitboxButton = buttons.get(direction);
		return button != null && button.pressed;
	}

	public function justPressed(direction:HitboxDirection):Bool
	{
		var button:HitboxButton = buttons.get(direction);
		return button != null && button.justPressed;
	}

	public function justReleased(direction:HitboxDirection):Bool
	{
		var button:HitboxButton = buttons.get(direction);
		return button != null && button.justReleased;
	}

	public function setDebugVisible(visible:Bool):Void
	{
		for (button in buttons)
			button.alpha = visible ? 0.4 : 0.00001;
	}

	public function anyPressed():Bool
	{
		for (button in buttons)
		{
			if (button.pressed)
				return true;
		}
		return false;
	}

	public function anyJustPressed():Bool
	{
		for (button in buttons)
		{
			if (button.justPressed)
				return true;
		}
		return false;
	}
}
