/**
  * This sketch demonstrates how to use the <code>addListener</code> method of a <code>Recordable</code> class. 
  * The class used here is <code>AudioPlayer</code>, but you can also add listeners to <code>AudioInput</code>, 
  * <code>AudioOutput</code>, and <code>AudioSample</code> objects. The class defined in waveform.pde implements 
  * the <code>AudioListener</code> interface and can therefore be added as a listener to <code>groove</code>.
  */

import ddf.minim.*;
import ddf.minim.analysis.*;
Minim minim;
AudioPlayer groove;
AudioInput in;
WaveformRenderer waveform;
void setup()
{
  size(800 , 600);
  minim = new Minim(this);
    in = minim.getLineIn(Minim.STEREO, 1024,44100);
PitchDetectorHPS pitch=new PitchDetectorHPS(1024,44100,5);
 // groove = minim.loadFile("groove.mp3", 512);
 waveform = new WaveformRenderer(pitch,8000);
    in.addListener(waveform);
 background(0);smooth();
 
  //groove.addListener(waveform);
}
float scal=0.5;
 void keyPressed(){
   {
    if( (key == '+' || key=='='))
    scal*=1.2f;
    if( (key == '-' || key=='_'))
    scal/=1.2f;
  }
 }
void draw()
{
 // background(0);
  
//ellipse(particleM.X,particleM.Y,5,5);
  waveform.draw();
 
}

void stop()
{
  // always close Minim audio classes when you are done with them
  //groove.close();
  // always stop Minim before exiting.
 // minim.stop();
 // super.stop();
}
void mouseDragged()
{

}


