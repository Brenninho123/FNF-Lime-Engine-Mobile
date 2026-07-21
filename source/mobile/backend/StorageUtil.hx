package mobile.backend;

import sys.FileSystem;
import sys.io.File;
import sys.io.FileOutput;
import sys.io.FileInput;
import haxe.io.Bytes;
import haxe.io.Path;
import haxe.Json;

#if android
import lime.system.JNI;
#end

class StorageUtil
{
	public static var baseDirectory(get, never):String;
	public static var externalDirectory(get, never):String;

	private static var permissionsRequested:Bool = false;

	private static function get_baseDirectory():String
	{
		#if android
		return getAndroidExternalPath() + "/";
		#elseif ios
		return getIOSDocumentsPath() + "/";
		#else
		return Sys.getCwd();
		#end
	}

	private static function get_externalDirectory():String
	{
		#if android
		return "/storage/emulated/0/";
		#else
		return baseDirectory;
		#end
	}

	#if android
	private static var _getExternalStorageDirectoryJNI:Dynamic;
	private static var _requestPermissionJNI:Dynamic;
	private static var _hasPermissionJNI:Dynamic;

	private static function getAndroidExternalPath():String
	{
		if (_getExternalStorageDirectoryJNI == null)
		{
			_getExternalStorageDirectoryJNI = JNI.createStaticMethod(
				"android/os/Environment",
				"getExternalStorageDirectory",
				"()Ljava/io/File;"
			);
		}

		var fileObj:Dynamic = _getExternalStorageDirectoryJNI();
		if (fileObj == null)
			return lime.system.System.applicationStorageDirectory;

		var toStringMethod = JNI.createMemberMethod("java/io/File", "getAbsolutePath", "()Ljava/lang/String;");
		var path:String = toStringMethod(fileObj);
		return path != null ? path : lime.system.System.applicationStorageDirectory;
	}

	public static function hasExternalStoragePermission():Bool
	{
		try
		{
			if (_hasPermissionJNI == null)
			{
				_hasPermissionJNI = JNI.createStaticMethod(
					"org/haxe/extension/ExtensionPermission",
					"checkPermission",
					"(Ljava/lang/String;)Z"
				);
			}
			return _hasPermissionJNI("android.permission.WRITE_EXTERNAL_STORAGE");
		}
		catch (e:Dynamic)
		{
			return true;
		}
	}

	public static function requestExternalStoragePermission():Void
	{
		if (permissionsRequested)
			return;

		permissionsRequested = true;

		try
		{
			if (_requestPermissionJNI == null)
			{
				_requestPermissionJNI = JNI.createStaticMethod(
					"org/haxe/extension/ExtensionPermission",
					"requestPermission",
					"(Ljava/lang/String;)V"
				);
			}
			_requestPermissionJNI("android.permission.WRITE_EXTERNAL_STORAGE");
			_requestPermissionJNI("android.permission.READ_EXTERNAL_STORAGE");
		}
		catch (e:Dynamic) {}
	}
	#end

	#if ios
	private static function getIOSDocumentsPath():String
	{
		return lime.system.System.documentsDirectory;
	}
	#end

	public static function ensureDirectory(path:String):Bool
	{
		var fullPath:String = resolvePath(path);
		try
		{
			if (!FileSystem.exists(fullPath))
			{
				FileSystem.createDirectory(fullPath);
			}
			return true;
		}
		catch (e:Dynamic)
		{
			return false;
		}
	}

	public static function resolvePath(path:String):String
	{
		if (Path.isAbsolute(path))
			return path;

		return Path.join([baseDirectory, path]);
	}

	public static function exists(path:String):Bool
	{
		return FileSystem.exists(resolvePath(path));
	}

	public static function isDirectory(path:String):Bool
	{
		var fullPath:String = resolvePath(path);
		return FileSystem.exists(fullPath) && FileSystem.isDirectory(fullPath);
	}

	public static function writeString(path:String, content:String):Bool
	{
		var fullPath:String = resolvePath(path);
		try
		{
			ensureDirectory(Path.directory(fullPath));
			File.saveContent(fullPath, content);
			return true;
		}
		catch (e:Dynamic)
		{
			return false;
		}
	}

	public static function readString(path:String, ?defaultValue:String = null):String
	{
		var fullPath:String = resolvePath(path);
		try
		{
			if (!FileSystem.exists(fullPath))
				return defaultValue;

			return File.getContent(fullPath);
		}
		catch (e:Dynamic)
		{
			return defaultValue;
		}
	}

	public static function writeBytes(path:String, bytes:Bytes):Bool
	{
		var fullPath:String = resolvePath(path);
		try
		{
			ensureDirectory(Path.directory(fullPath));
			File.saveBytes(fullPath, bytes);
			return true;
		}
		catch (e:Dynamic)
		{
			return false;
		}
	}

	public static function readBytes(path:String):Bytes
	{
		var fullPath:String = resolvePath(path);
		try
		{
			if (!FileSystem.exists(fullPath))
				return null;

			return File.getBytes(fullPath);
		}
		catch (e:Dynamic)
		{
			return null;
		}
	}

	public static function writeJson(path:String, data:Dynamic, pretty:Bool = false):Bool
	{
		var content:String = pretty ? Json.stringify(data, null, "\t") : Json.stringify(data);
		return writeString(path, content);
	}

	public static function readJson<T>(path:String, ?defaultValue:T = null):T
	{
		var content:String = readString(path, null);
		if (content == null)
			return defaultValue;

		try
		{
			return Json.parse(content);
		}
		catch (e:Dynamic)
		{
			return defaultValue;
		}
	}

	public static function delete(path:String):Bool
	{
		var fullPath:String = resolvePath(path);
		try
		{
			if (!FileSystem.exists(fullPath))
				return true;

			if (FileSystem.isDirectory(fullPath))
			{
				clearDirectory(fullPath);
				FileSystem.deleteDirectory(fullPath);
			}
			else
			{
				FileSystem.deleteFile(fullPath);
			}
			return true;
		}
		catch (e:Dynamic)
		{
			return false;
		}
	}

	public static function clearDirectory(path:String):Bool
	{
		var fullPath:String = resolvePath(path);
		try
		{
			if (!FileSystem.exists(fullPath) || !FileSystem.isDirectory(fullPath))
				return false;

			for (entry in FileSystem.readDirectory(fullPath))
			{
				var entryPath:String = Path.join([fullPath, entry]);
				if (FileSystem.isDirectory(entryPath))
				{
					clearDirectory(entryPath);
					FileSystem.deleteDirectory(entryPath);
				}
				else
				{
					FileSystem.deleteFile(entryPath);
				}
			}
			return true;
		}
		catch (e:Dynamic)
		{
			return false;
		}
	}

	public static function list(path:String):Array<String>
	{
		var fullPath:String = resolvePath(path);
		try
		{
			if (!FileSystem.exists(fullPath) || !FileSystem.isDirectory(fullPath))
				return [];

			return FileSystem.readDirectory(fullPath);
		}
		catch (e:Dynamic)
		{
			return [];
		}
	}

	public static function copy(sourcePath:String, destPath:String):Bool
	{
		var fullSource:String = resolvePath(sourcePath);
		var fullDest:String = resolvePath(destPath);
		try
		{
			if (!FileSystem.exists(fullSource))
				return false;

			ensureDirectory(Path.directory(fullDest));

			if (FileSystem.isDirectory(fullSource))
			{
				ensureDirectory(fullDest);
				for (entry in FileSystem.readDirectory(fullSource))
				{
					copy(Path.join([fullSource, entry]), Path.join([fullDest, entry]));
				}
			}
			else
			{
				File.copy(fullSource, fullDest);
			}
			return true;
		}
		catch (e:Dynamic)
		{
			return false;
		}
	}

	public static function move(sourcePath:String, destPath:String):Bool
	{
		if (copy(sourcePath, destPath))
		{
			return delete(sourcePath);
		}
		return false;
	}

	public static function getFileSize(path:String):Int
	{
		var fullPath:String = resolvePath(path);
		try
		{
			if (!FileSystem.exists(fullPath))
				return -1;

			return FileSystem.stat(fullPath).size;
		}
		catch (e:Dynamic)
		{
			return -1;
		}
	}

	public static function getModifiedTime(path:String):Date
	{
		var fullPath:String = resolvePath(path);
		try
		{
			if (!FileSystem.exists(fullPath))
				return null;

			return FileSystem.stat(fullPath).mtime;
		}
		catch (e:Dynamic)
		{
			return null;
		}
	}

	public static function rename(path:String, newName:String):Bool
	{
		var fullPath:String = resolvePath(path);
		var newPath:String = Path.join([Path.directory(fullPath), newName]);
		try
		{
			FileSystem.rename(fullPath, newPath);
			return true;
		}
		catch (e:Dynamic)
		{
			return false;
		}
	}

	public static function getAvailableSpace():Float
	{
		#if android
		try
		{
			var statFsClass = JNI.createStaticMethod(
				"org/haxe/extension/ExtensionStorage",
				"getAvailableSpace",
				"()J"
			);
			return statFsClass();
		}
		catch (e:Dynamic)
		{
			return -1;
		}
		#else
		return -1;
		#end
	}
}
