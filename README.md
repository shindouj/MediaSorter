# MediaSorter
One utility to sort them all.

## What is MediaSorter?
MediaSorter is a fancy utility for sorting all kinds of files. It uses a custom XML filter scheme for full customization for all your needs - MediaSorter does what you tell it to do. It's also modular, allowing powerusers to easily add new features by adding a small module.

MediaSorter uses three simple module types for operation:
* **Filter** - as the name suggests, it filters out files basing on its input. For example, PrefixFileFilter filters files by their prefix. Easy as that. You can add new filters by simply adding a new class and registering it in FilterHandler.
* **Parser** - it parses file name to VideoMetadata. Used mainly for video, may be extended for generic use (FileMetadata interface). Can be simply added by registering in ParserHandler.
* **MediaDBConnector** - connects to a given online media database and confirms its type (eg. connects to IMDB and confirms that the file given is a movie, not a TV series). Currently supported media types are MOVIE, SERIES and ANIME, may be extended in the future.

## Do you need help with the project?
Absolutely. First things first, this tool needs decent Java parsers instead of using ready-made parsers in other languages. I'm not very good with RegExps and parsing is not my favourite thing to do (also yes, I tried to port them, and I failed :<), so I worked around it by embedding parsers made by other people in other languages, which are very good, but you know - it's not the smartest and cleanest thing to do.

## Contributions
This project is using [parse-torrent-name](https://github.com/divijbindlish/parse-torrent-name) by [Divij Bindlish](http://divijbindlish.in), licensed under MIT license. 
