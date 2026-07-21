package;

import flixel.FlxState;

#if mobile
import mobile.controls.Hitbox;
import mobile.controls.HitboxDirection;
#end

class PlayState extends FlxState
{
	#if mobile
	var hitbox:Hitbox;
	#end

	override public function create()
	{
		super.create();

		#if mobile
		hitbox = new Hitbox();
		add(hitbox);
		#end
	}

	override public function update(elapsed:Float)
	{
		super.update(elapsed);

		#if mobile
		if (hitbox.justPressed(LEFT))
		{
		}

		if (hitbox.justPressed(DOWN))
		{
		}

		if (hitbox.justPressed(UP))
		{
		}

		if (hitbox.justPressed(RIGHT))
		{
		}
		#end
	}
}
