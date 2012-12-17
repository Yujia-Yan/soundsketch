
class Brush{
Particle[] particle;
Particle particleM=new Particle(random(0,width),random(0,height),random(0,100),0.3);
Brush(){
  
particle=new Particle[500];
  for(int i=0;i<particle.length;i++){
    particle[i]=new Particle(random(0,width),random(0,height),random(0,1000),random(0.1,5));
    particle[i].setV(random(0,100),random(0,100),0);
  }
}
  void draw(){
    stroke(255, 255, 255, 5);
  strokeWeight(1);
 // particleM.X+=(0.03*(mouseX-particleM.X));
 // particleM.Y+=(0.03*(mouseY-particleM.Y));
    for(int i=0;i<particle.length;i++){  
    particle[i].applyForce(0.03*(particleM.X-particle[i].X),0.03*(particleM.Y-particle[i].Y),0);
    particle[i].draw();
    }
  }
  void pull(float X,float Y,float force){
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
  float damp=0.95;
  float M;
  Particle(float X,float Y,float Z,float M){
    this.X=X;
    this.Y=Y;
    this.Z=Z;
    this.M=M;
  }
  void setV(float Vx,float Vy,float Vz){
    this.Vx=Vx;
    this.Vy=Vy;
    this.Vz=Vz;
  }
  void applyForce(float Fx,float Fy,float Fz){
    Vx*=damp;
    Vy*=damp;
    Vx+=Fx/M;
    Vy+=Fy/M;
    Vz+=Fz/M;
  }
  void draw(){

    X+=Vx;
    Y+=Vy;
    Z+=Vz;
    line(X,Y,X-Vx,Y-Vy);
  }
  
}
