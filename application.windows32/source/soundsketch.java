import processing.core.*; 
import processing.data.*; 
import processing.opengl.*; 

import ddf.minim.*; 
import ddf.minim.analysis.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class soundsketch extends PApplet {





Minim minim;
AudioPlayer groove;
AudioInput in;
WaveformRenderer waveform;
public void setup()
{
  size(800 , 600);
  minim = new Minim(this);
    in = minim.getLineIn(Minim.STEREO, 1024,44100);
SpecturmFeature feature=new SpecturmFeature(1024,44100,10);
 // groove = minim.loadFile("groove.mp3", 512);
 waveform = new WaveformRenderer(feature,8000);
    in.addListener(waveform);
 background(0);smooth();
 
  //groove.addListener(waveform);
}
float scal=0.5f;
 public void keyPressed(){
   {
    if( (key == '+' || key=='='))
    scal*=1.2f;
    if( (key == '-' || key=='_'))
    scal/=1.2f;
  }
 }
public void draw()
{
 // background(0);
  
//ellipse(particleM.X,particleM.Y,5,5);
  waveform.draw();
 
}

public void stop()
{
  // always close Minim audio classes when you are done with them
  //groove.close();
  // always stop Minim before exiting.
 // minim.stop();
 // super.stop();
}
public void mouseClicked()
{
   saveFrame("frame-######.jpg");
}



class Brush{
Particle[] particle;
Particle particleM=new Particle(random(0,width),random(0,height),random(0,100),0.3f);
Brush(){
  
particle=new Particle[500];
  for(int i=0;i<particle.length;i++){
    particle[i]=new Particle(random(0,width),random(0,height),random(0,1000),random(0.1f,5));
    particle[i].setV(random(0,100),random(0,100),0);
  }
}
  public void draw(float H,float S,float B,float m){
   // colorMode(HSB, 360, 1, 1);
    stroke(255,255,255,5);
  strokeWeight(1);
 // particleM.X+=(0.03*(mouseX-particleM.X));
 // particleM.Y+=(0.03*(mouseY-particleM.Y));
    for(int i=0;i<particle.length;i++){  
    particle[i].applyForce(0.03f*m*(particleM.X-particle[i].X),0.03f*m*(particleM.Y-particle[i].Y),0);
    particle[i].draw();
    }
  }
  public void pull(float X,float Y,float force){
    particleM.X+=force*(X-particleM.X);
    particleM.Y+=force*(Y-particleM.Y);
  }
}
class Particle{
  float X;
  float Y;
  float Z;
  float Vx;
  float Vy;
  float Vz;
  float damp=0.95f;
  float M;
  Particle(float X,float Y,float Z,float M){
    this.X=X;
    this.Y=Y;
    this.Z=Z;
    this.M=M;
  }
  public void setV(float Vx,float Vy,float Vz){
    this.Vx=Vx;
    this.Vy=Vy;
    this.Vz=Vz;
  }
  public void applyForce(float Fx,float Fy,float Fz){
    Vx*=damp;
    Vy*=damp;
    Vx+=Fx/M;
    Vy+=Fy/M;
    Vz+=Fz/M;
  }
  public void draw(){

    X+=Vx;
    Y+=Vy;
    Z+=Vz;
    line(X,Y,X-Vx,Y-Vy);
  }
  
}
class SpecturmFeature{
  // some features need to be converted to dB scale
  FFT fft;
  int sampleRate;
  int binLength;
  int harmonicSize;
  int fftLength;
  float[][] step;
  float[] frame;
  float maxfrequency;
  public void setFrame(float[] frame){
    this.frame=frame;
    fft.forward(frame);
  }
  SpecturmFeature(int fftLength,int sampleRate,int harmonicSize){
    fft=new FFT(fftLength,sampleRate);
    maxfrequency=fft.specSize()*fft.getBandWidth();

    this.fftLength=fftLength;
    this.sampleRate=sampleRate;
    this.binLength=fft.specSize();
    //this.fftLength=fftLength;
    this.harmonicSize=harmonicSize;
    step=new float[harmonicSize][];
    for(int i=0;i<harmonicSize;i++){
      step[i]=new float[binLength];
    }
    //fft.window(fft.HAMMING);
  }
  public float pitchDetect(){
    //downsample
      for(int i=0;i<binLength;i++){
        for(int j=1;j<=harmonicSize;j++){
           if(i%j==0)( step[j-1])[i/j]=fft.getBand(i);
        }
      }
     // println(fft.specSize());
    //HSP
      int index=0;
      float max;
      max=0;
      float tmp;;
      for(int i=0;i<binLength;i++){
        tmp=1;
        for(int j=0;j<harmonicSize;j++){
          tmp*=step[j][i];
        }
        //println(tmp);
        if(tmp>max){
          max=tmp;
          index=i;
        }
      }
       if(index==0)return 1.0f;
      // println( index*fft.getBandWidth());
      return index*fft.getBandWidth();
   

  }
  public float centroid(){
    float wSum=0;
    float aSum=0;
     for(int i=1;i<binLength;i++){
          wSum+=i*fft.getBand(i);
          aSum+=fft.getBand(i);
     }
     //println((wSum/aSum)*fft.getBandWidth());
     return wSum/aSum*fft.getBandWidth();
  }
  public float spread(){
    float sc=centroid()/fft.getBandWidth();
     float wSum=0;
    float aSum=0;
     for(int i=1;i<binLength;i++){
        
          wSum+=((i-sc)*(i-sc))*fft.getBand(i);
          aSum+=fft.getBand(i);
     }
     //println((wSum/aSum)*fft.getBandWidth());
     float result=wSum/aSum*fft.getBandWidth()*fft.getBandWidth()/binLength/binLength/binLength;
    // println(result);
     return result;
     
  }
  
  public float ZCR(){
    int zeros=0;
     for(int i=0;i<frame.length-1;i++){
       if(frame[i]*frame[i+1]<0) zeros++;
     }
    // println( (float)zeros/2/frame.length);
     return (float)zeros/2/frame.length;
  }
  public float RMS(){
    float power=0;
     for(int i=0;i<frame.length;i++){
       power+=frame[i]*frame[i];
     }
     return power/frame.length;
  }
  public float flatness(){
    //stub?unit
    float logsum=0;
    float sum=0;
     for(int i=1;i<binLength;i++){
      logsum+=log( fft.getBand(i));
      sum+=fft.getBand(i);
     }
     float result=exp(logsum/binLength)/sum*binLength;
     //println(result);
     return result;
  }
  public float mel2freq(float freq){
    return 1127.01028f*log(1+freq/700);
  }
  public float freq2mel(float mel){
    return 700*(exp(mel/1127.01028f)-1);
  }
  public float[] Hamming(int n){
     float[] result=new float[n];
     for(int i=0;i<n;i++){
       result[i]=0.5f*(1.0f-cos(2.0f*(float)PI*i/(n-1)));
     }
     return result;
  }
  float[] melFilterCenter;
  public void setMelFilterAmount(int melFilterAmount){
    int maxMel=ceil(freq2mel(maxfrequency));
    if(melFilterCenter!=null&&melFilterCenter.length!=melFilterAmount){
      melFilterCenter=new float[melFilterAmount];
      for(int i=0;i<melFilterCenter.length;i++){
         // melFilterCenter[i]=mel2freq(i*)
      }
    }
    
    
  }

 
}


class WaveformRenderer implements AudioListener
{
  

Brush brush=new Brush();
  float prev=0;
   float prev2=0;
  float prev3=0;
 float freq=0;
  private float[] left;
  private float[] right;
  float partialCount;
  int sampleRate;
  SpecturmFeature feature;
  WaveformRenderer(SpecturmFeature feature,int sampleRate)
  {
    left = null;
    right = null;
    this.feature=feature;
    this.sampleRate=sampleRate;
  }
  
  public synchronized void samples(float[] samp){
    left=samp;
  }

  public synchronized void samples(float[] sampL, float[] sampR)
  {
    
     feature.setFrame(sampL);
    freq=freq*0.2f+0.8f*feature.pitchDetect();
   
    //println(freq);
    left = sampL;
    right = sampR;
  }
  float maxH=0;
  float maxS=0;
  float maxB=0;
    float minH=300;
  float minS=300;
  float minB=300;
  public synchronized void draw()
  {
  
      float m=0;
    // we've got a stereo signal if right or left are not null
    if ( left != null && right != null )
    {
     for ( int i = 0; i < left.length; i++ )
     {
       if(abs(left[i])>m)m=abs(left[i]);
     }
     if(m<0.01f){
     //brush.pull(0,0,0.3);
     return;
   }
     // noFill();
      stroke(0);
      float tmp=0;
      float tmp2=0;
      float []xxx=new float[left.length];
      float []yyy=new float[left.length];
      for ( int i = 0; i < left.length; i++ )
      {
        tmp=left[i]-prev;
        tmp2=tmp-prev2;
         xxx[i]=left[i]*600/m;
        yyy[i]=200*(tmp)/m/freq/PI*sampleRate;
        
        //point(left[i]*600/m,200*(tmp)/m/freq/PI*sampleRate);
        prev2=tmp;
        prev3=tmp2;
        prev=left[i];
        
      }
      float p=(12*log(freq/440)+69);
      
      float l=10*log(m*m/1e-12f)*log(2)/log(10);
      l=norm(l,50,90);
      p=norm(p,20,70);
      brush.pull(l*width,height-height*p,1);
     // ellipse(l*width,height-height*p,5,5);
      //println(height*p);
      float H=sqrt(feature.spread())*360;
      float S=feature.flatness();
      float B=feature.centroid()/feature.maxfrequency;
      if(H>maxH)maxH=H;
      if(S>maxS)maxS=S;
      if(B>maxB)maxB=B;
       if(H<minH)minH=H;
      if(S<minS)minS=S;
      if(B<minB)minB=B;
      H=(H)%360;
      S=(S*2);
      B=(B*2);
      //println(m);
      brush.draw(H,S,B,m);
     // println(maxH+"   "+maxS+"   "+ maxB);
      // println(minH+"   "+minS+"   "+ minB);
    }
  }
  
  

}

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "soundsketch" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
