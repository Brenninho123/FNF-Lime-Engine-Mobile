package backend;

import flixel.graphics.frames.FlxAtlasFrames;
import flixel.graphics.FlxGraphic;
import flixel.math.FlxRect;

import openfl.display.BitmapData;
import openfl.utils.AssetType;
import openfl.utils.Assets as OpenFlAssets;
import openfl.system.System;

import flash.media.Sound;

import haxe.Json;
import haxe.io.Path;

import mobile.backend.StorageUtil;

#if MODS_ALLOWED
import backend.Mods;
#end

class Paths
{
	inline public static var SOUND_EXT = #if web "wav" #else "ogg" #end;
	inline public static var VIDEO_EXT = "mp4";

	public static var dumpExclusions:Array<String> = ['assets//music/freakyMenu.$SOUND_EXT'];

	public static var currentLevel:String;

	private static var currentTrackedSounds:Map<String, Sound> = new Map<String, Sound>();
	private static var currentTrackedTextures:Map<String, FlxGraphic> = new Map<String, FlxGraphic>();
	private static var localTrackedAssets:Array<String> = [];

	public static function excludeAsset(key:String)
	{
		if (!dumpExclusions.contains(key))
			dumpExclusions.push(key);
	}

	public static function setCurrentLevel(name:String):Void
	{
		currentLevel = name.toLowerCase();
	}

	public static function getPreloadPath(file:String = ''):String
	{
		return 'assets/$file';
	}

	#if MODS_ALLOWED
	public static function mods(key:String = ''):String
	{
		return 'mods/' + key;
	}

	public static function modsJson(key:String):String
	{
		return mods('data/$key.json');
	}

	public static function modsSound(key:String):String
	{
		return mods('sounds/$key.$SOUND_EXT');
	}

	public static function modsMusic(key:String):String
	{
		return mods('music/$key.$SOUND_EXT');
	}

	public static function modsImage(key:String):String
	{
		return mods('images/$key.png');
	}

	public static function modsFont(key:String):String
	{
		return mods('fonts/$key');
	}

	public static function modsExternalDirectory():String
	{
		#if android
		return StorageUtil.externalDirectory + 'FNFLimeEngine/mods/';
		#else
		return StorageUtil.resolvePath('mods/');
		#end
	}

	public static function ensureModsDirectory():Void
	{
		#if android
		StorageUtil.ensureDirectory('FNFLimeEngine/mods');
		#else
		StorageUtil.ensureDirectory('mods');
		#end
	}

	public static function listExternalMods():Array<String>
	{
		var dir:String = modsExternalDirectory();
		if (!StorageUtil.isDirectory(dir))
			return [];

		return StorageUtil.list(dir).filter(function(entry)
		{
			return StorageUtil.isDirectory(Path.join([dir, entry]));
		});
	}
	#end

	public static function file(file:String, type:AssetType = null, ?library:String = null):String
	{
		if (library != null)
			return getLibraryPath(file, library);

		return getPreloadPath(file);
	}

	inline static public function getLibraryPath(file:String, library:String = 'shared'):String
	{
		return 'assets/$library/$file';
	}

	static public function fileExists(path:String, useMods:Bool = true):Bool
	{
		#if MODS_ALLOWED
		if (useMods)
		{
			var modPath:String = mods(path);
			if (StorageUtil.exists(modPath))
				return true;
		}
		#end

		return OpenFlAssets.exists(path) || StorageUtil.exists(path);
	}

	inline static public function json(key:String):Dynamic
	{
		var rawJson:String = null;

		#if MODS_ALLOWED
		var modPath:String = modsJson(key);
		if (StorageUtil.exists(modPath))
			rawJson = StorageUtil.readString(modPath);
		#end

		if (rawJson == null)
		{
			var assetPath:String = getPreloadPath('data/$key.json');
			if (OpenFlAssets.exists(assetPath))
				rawJson = OpenFlAssets.getText(assetPath);
		}

		if (rawJson == null)
			return null;

		return Json.parse(rawJson);
	}

	inline static public function txt(key:String, ?library:String):String
	{
		return getPath('$key.txt', TEXT, library);
	}

	inline static public function xml(key:String, ?library:String):String
	{
		return getPath('$key.xml', TEXT, library);
	}

	inline static public function fragShader(key:String):String
	{
		return getPath('shaders/$key.frag', TEXT);
	}

	inline static public function vertShader(key:String):String
	{
		return getPath('shaders/$key.vert', TEXT);
	}

	static public function video(key:String):String
	{
		#if MODS_ALLOWED
		var modPath:String = mods('videos/$key.$VIDEO_EXT');
		if (StorageUtil.exists(modPath))
			return modPath;
		#end

		return 'assets/videos/$key.$VIDEO_EXT';
	}

	inline static public function getSparrowAtlas(key:String, ?library:String):FlxAtlasFrames
	{
		return FlxAtlasFrames.fromSparrow(image(key, library), file('images/$key.xml', TEXT, library));
	}

	inline static public function getPackerAtlas(key:String, ?library:String):FlxAtlasFrames
	{
		return FlxAtlasFrames.fromSpriteSheetPacker(image(key, library), file('images/$key.txt', TEXT, library));
	}

	static public function getPath(file:String, type:AssetType, ?library:String):String
	{
		if (library != null)
			return getLibraryPath(file, library);

		return getPreloadPath(file);
	}

	static public function sound(key:String, ?library:String):Sound
	{
		#if MODS_ALLOWED
		var modPath:String = modsSound(key);
		if (StorageUtil.exists(modPath))
		{
			return resolveExternalSound(modPath);
		}
		#end

		var soundPath:String = getPath('sounds/$key.$SOUND_EXT', SOUND, library);
		return cacheSound(soundPath);
	}

	static public function music(key:String, ?library:String):Sound
	{
		#if MODS_ALLOWED
		var modPath:String = modsMusic(key);
		if (StorageUtil.exists(modPath))
		{
			return resolveExternalSound(modPath);
		}
		#end

		var musicPath:String = getPath('music/$key.$SOUND_EXT', MUSIC, library);
		return cacheSound(musicPath);
	}

	static function cacheSound(path:String):Sound
	{
		if (currentTrackedSounds.exists(path))
			return currentTrackedSounds.get(path);

		var loadedSound:Sound = OpenFlAssets.getSound(path);
		if (loadedSound != null)
		{
			currentTrackedSounds.set(path, loadedSound);
			localTrackedAssets.push(path);
		}
		return loadedSound;
	}

	#if MODS_ALLOWED
	static function resolveExternalSound(path:String):Sound
	{
		if (currentTrackedSounds.exists(path))
			return currentTrackedSounds.get(path);

		var bytes = StorageUtil.readBytes(path);
		if (bytes == null)
			return null;

		var loadedSound:Sound = new Sound();
		#if sys
		loadedSound.loadCompressedDataFromByteArray(bytes.getData(), bytes.length);
		#end

		currentTrackedSounds.set(path, loadedSound);
		localTrackedAssets.push(path);
		return loadedSound;
	}
	#end

	static public function image(key:String, ?library:String):FlxGraphic
	{
		#if MODS_ALLOWED
		var modPath:String = modsImage(key);
		if (StorageUtil.exists(modPath))
		{
			return resolveExternalImage(modPath);
		}
		#end

		var imagePath:String = getPath('images/$key.png', IMAGE, library);
		return cacheImage(imagePath);
	}

	static function cacheImage(path:String):FlxGraphic
	{
		if (currentTrackedTextures.exists(path))
			return currentTrackedTextures.get(path);

		var bitmapData:BitmapData = OpenFlAssets.getBitmapData(path);
		if (bitmapData == null)
			return null;

		var graphic:FlxGraphic = FlxGraphic.fromBitmapData(bitmapData, false, path);
		graphic.persist = true;
		graphic.destroyOnNoUse = false;

		currentTrackedTextures.set(path, graphic);
		localTrackedAssets.push(path);
		return graphic;
	}

	#if MODS_ALLOWED
	static function resolveExternalImage(path:String):FlxGraphic
	{
		if (currentTrackedTextures.exists(path))
			return currentTrackedTextures.get(path);

		var bytes = StorageUtil.readBytes(path);
		if (bytes == null)
			return null;

		var bitmapData:BitmapData = BitmapData.fromBytes(bytes);
		if (bitmapData == null)
			return null;

		var graphic:FlxGraphic = FlxGraphic.fromBitmapData(bitmapData, false, path);
		graphic.persist = true;
		graphic.destroyOnNoUse = false;

		currentTrackedTextures.set(path, graphic);
		localTrackedAssets.push(path);
		return graphic;
	}
	#end

	inline static public function font(key:String):String
	{
		#if MODS_ALLOWED
		var modPath:String = modsFont(key);
		if (StorageUtil.exists(modPath))
			return modPath;
		#end

		return 'assets/fonts/$key';
	}

	static public function clearUnusedMemory():Void
	{
		for (key in currentTrackedSounds.keys())
		{
			if (localTrackedAssets.indexOf(key) == -1)
				currentTrackedSounds.remove(key);
		}

		for (key in currentTrackedTextures.keys())
		{
			if (localTrackedAssets.indexOf(key) == -1)
			{
				var graphic:FlxGraphic = currentTrackedTextures.get(key);
				if (graphic != null && !dumpExclusions.contains(key))
					graphic.destroy();
				currentTrackedTextures.remove(key);
			}
		}

		localTrackedAssets = [];
		System.gc();
	}

	static public function clearStoredMemory():Void
	{
		currentTrackedSounds.clear();
		currentTrackedTextures.clear();
		localTrackedAssets = [];
		System.gc();
	}
}
