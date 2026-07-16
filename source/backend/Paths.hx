package backend;

import flixel.graphics.frames.FlxFrame.FlxFrameAngle;
import flixel.graphics.frames.FlxAtlasFrames;
import flixel.graphics.FlxGraphic;
import flixel.math.FlxRect;

import openfl.display.BitmapData;
import openfl.display3D.textures.RectangleTexture;
import openfl.utils.AssetType;
import openfl.utils.Assets as OpenFlAssets;
import openfl.system.System;
import openfl.geom.Rectangle;

import lime.utils.Assets;
import flash.media.Sound;

import haxe.Json;

#IF MODS_ALLOWED
import backend.Mods;
#end

class Paths
{
    inline public static var SOUND_EXT = #if web "wav" #else "ogg" #end;
    inline public static var VIDEO_EXT = "mp4";

	public static function excludeAsset(key:String) {
    if (!dumpExclusions.contains(key))
			dumpExclusions.push(key);
    }
    
    public static var dumpExclusions:Array<String> = ['assets//music/freakyMenu.$SOUND_EXT'];

    
}
