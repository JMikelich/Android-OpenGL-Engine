# Android OpenGL ES2 Multi-Activity Engine
A very basic and simple OpenGL ES2 engine which incorporates a GLText function and smooth multi-activity switching.

Overview: 
The purpose for this engine was to serve as a basic framework for my to-do list making app Jamie's To Do. 
The difficulty was requiring usage of the device keyboard while maintaining the UI Jamie's To Do has on the iOS.
After trying multiple platforms (which either had the keyboard but the UI wasn't sufficient, or the UI was sufficient 
but the keyboard wasn't intergraded), I decided that a simple OpenGL platform was the way to go.

Description: 
The engine is set up to deal with loading textures, both spritesheet or single unit, and using sprites to 
bind and display. The engine also is designed to do this as simply as possible.

One of the largest difficulties with OpenGL is displaying clear and readable font loaded from any truetype font file. 
As stated, the open source GLText written by Jocelyn Demoy (https://github.com/jodem/glText-Android) creates a seamless 
and crisp way to display text within an OpenGL activity. The GLText class has been incorporated into this engine, special 
thanks to Jodem and Fractious Games for created such a robust class.

Another large issue is incorporated OpenGL and the native Android classes. On iOS this is very easy by creating a 
transparent top layer XIB to house all native API's, but Android doesn't allow that. This engine saves the OpenGL context 
and allows the engine to seamlessly switch between multiple Android native class XML Activities and their OpenGL Activity. 
The OpenGL employs ES2, basic shaders, basic textures, and sprite sheets.

Usage: 
The app is set up to run in Eclipse with some basic examples of usage. If the app user touches the screen in the 
top 4/5 of the screen, the LanziVision logo will be centered on the users touch location. If the user touches the bottom 
1/5 of the screen, the OpenGL activity will switch with the Android XML activity (saving the OpenGL context as it does).
The Android activity is set up with a text description, the LanziVision logo, and a button to return to the OpenGL activity.

Licensing: 
This engine is open source, feel free to use it in any commercial or personal project without feeling need to
credit me or LanziVision. If you have any questions feel free to ask them at any time.
