
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
  
  synchronized void samples(float[] samp){
    left=samp;
  }

  synchronized void samples(float[] sampL, float[] sampR)
  {
    
     feature.setFrame(sampL);
    freq=freq*0.2+0.8*feature.pitchDetect();
   
    //println(freq);
    left = sampL;
    right = sampR;
  }
  
  synchronized void draw()
  {

      float m=0;
    // we've got a stereo signal if right or left are not null
    if ( left != null && right != null )
    {
     for ( int i = 0; i < left.length; i++ )
     {
       if(abs(left[i])>m)m=abs(left[i]);
     }
     if(m<0.01)return;
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
      
      float l=10*log(m*m/1e-12)*log(2)/log(10);
      l=norm(l,40,90);
      p=norm(p,30,80);
      brush.pull(l*width,height-height*p,1);
      //ellipse(l*width,height-height*p,5,5);
      //println(height*p);
      brush.draw();
      feature.centroid();
      feature.ZCR();
    }
  }
  
  

}

