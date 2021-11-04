import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class CirclularCircleConnections extends PApplet {

int gridsize = 40;
ArrayList<Orb> orbs = new ArrayList<Orb>();
ArrayList<FadingCircle> circles = new ArrayList<FadingCircle>();
int from;
int to;
public void setup() {
   
   background(255);
   from = color(0, 0, 0);
   to = color(255, 255, 255);

   for (int i = 1; i < gridsize; i++) { 
      for (int j = 1; j < gridsize; j++) {
         float x = width*i/gridsize;
         float y = height*j/gridsize;
         Orb orb = new Orb(x,y);
         orbs.add(orb);
         // orb.display();
      }
   }
}

public void draw() {
   background(255);
   for (int i = 0; i < orbs.size(); i++){
      Orb orb = orbs.get(i);
      orb.move();
   }
   draw_circle();
   draw_line();
   for (int i = circles.size()-1; i >= 0; i--) {
      FadingCircle circle = circles.get(i);
      if (circle.frames > 0){
         circle.display();
      } else{
         circles.remove(i);
      }
   }
}

public void draw_circle(){
   for (int i = 0; i < orbs.size(); i++) {
      Orb current_orb = orbs.get(i);
      for (int j = 0; j < orbs.size(); j++) {
         Orb test_orb = orbs.get(j);
         float dist = distance(current_orb.xpos, current_orb.ypos, test_orb.xpos, test_orb.ypos);
         float totaldist = (current_orb.orbsize/2.0f + test_orb.orbsize/2.0f);
         if (dist < totaldist){
            float halfx = (current_orb.xpos - test_orb.xpos)/2.0f + current_orb.xpos;
            float halfy = (current_orb.ypos - test_orb.ypos)/2.0f + current_orb.ypos;
            // float transparency = 255 * (dist/totaldist);
            int between = lerpColor(to, from, dist/totaldist);
            // fill(between);
            // noStroke();
            // ellipse(halfx, halfy, dist, dist);
            circles.add(new FadingCircle(halfx, halfy, between, dist));
         }
      }
   }
}


class FadingCircle {
   int c;
   float x;
   float y;
   float frames = 30;
   float size;

   public FadingCircle(float x0, float y0, int c0, float size0){
      x = x0;
      y = y0;
      c = c0;
      size = size0;
   }

   public void display(){
      float transparency = 255.0f * (frames / 20.0f);
      // fill(c, transparency);
      // noStroke();
      stroke(c, transparency);
      noFill();
      ellipse(x, y, size, size);
      frames -= 1.0f;
   }
}


public void draw_line(){
   for (int i = 0; i < orbs.size(); i++) {
      Orb current_orb = orbs.get(i);
      for (int j = 0; j < orbs.size(); j++) {
         Orb test_orb = orbs.get(j);
         if (distance(current_orb.xpos, current_orb.ypos, test_orb.xpos, test_orb.ypos) < (current_orb.orbsize/2.0f + test_orb.orbsize/2.0f)){
            stroke(0);
            line(current_orb.xpos, current_orb.ypos, test_orb.xpos, test_orb.ypos);
         }
      }
   }
}

class Orb {
   float orbsize;
   float x_speed;
   float y_speed;
   float xpos;
   float ypos;
   float speed = 0.5f;
   public Orb(float x, float y){
      orbsize = height/100.0f;
      // x_speed = random(-speed, speed);
      // y_speed = random(-speed, speed);
      float dirx = x - width/2;
      float diry = y - height/2;
      float mag = sqrt((dirx * dirx) + (diry * diry));
      dirx = dirx / mag;
      diry = diry / mag;
      x_speed = -dirx;
      y_speed = -diry;
      xpos = x;
      ypos = y;
   }
   
   public void display(){
      noStroke();
      fill(0);
      ellipse(xpos, ypos, orbsize, orbsize);
   }
   
   public void move(){
      if (xpos + x_speed + orbsize > width || xpos + x_speed - orbsize < 0){
         x_speed *= -1;
      }
      if (ypos + y_speed + orbsize > height || ypos + y_speed - orbsize < 0){
         y_speed *= -1;
      }

      xpos = xpos + x_speed;
      ypos = ypos + y_speed;
   } 
}

public float prop(float value){
   return value * width / 512;
}

public float distance(float x1, float y1, float x2, float y2){
   return (float)sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
}
  public void settings() {  size(512, 512); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "CirclularCircleConnections" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
