

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
SpecturmFeature feature=new SpecturmFeature(1024,44100,3);
 // groove = minim.loadFile("groove.mp3", 512);
 waveform = new WaveformRenderer(feature,8000);
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
  //background(0);
  
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
void mouseClicked()
{
   saveFrame("frame-######.jpg");
}


