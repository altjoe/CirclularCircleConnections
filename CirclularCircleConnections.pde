int gridsize = 20;
ArrayList<Orb> orbs = new ArrayList<Orb>();
ArrayList<FadingCircle> circles = new ArrayList<FadingCircle>();
color from;
color to;
void setup() {
   size(512, 512);
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

void draw() {
   background(255);
   for (int i = 0; i < orbs.size(); i++){
      Orb orb = orbs.get(i);
      orb.move();
   }
   draw_circle();
   // draw_line();
   for (int i = circles.size()-1; i >= 0; i--) {
      FadingCircle circle = circles.get(i);
      if (circle.frames > 0){
         circle.display();
      } else{
         circles.remove(i);
      }
   }
}

void draw_circle(){
   for (int i = 0; i < orbs.size(); i++) {
      Orb current_orb = orbs.get(i);
      for (int j = 0; j < orbs.size(); j++) {
         Orb test_orb = orbs.get(j);
         float dist = distance(current_orb.xpos, current_orb.ypos, test_orb.xpos, test_orb.ypos);
         float totaldist = (current_orb.orbsize/2.0 + test_orb.orbsize/2.0);
         if (dist < totaldist){
            float halfx = (current_orb.xpos - test_orb.xpos)/2.0 + current_orb.xpos;
            float halfy = (current_orb.ypos - test_orb.ypos)/2.0 + current_orb.ypos;
            // float transparency = 255 * (dist/totaldist);
            color between = lerpColor(to, from, dist/totaldist);
            // fill(between);
            // noStroke();
            // ellipse(halfx, halfy, dist, dist);
            circles.add(new FadingCircle(halfx, halfy, between, dist));
         }
      }
   }
}


class FadingCircle {
   color c;
   float x;
   float y;
   float frames = int(random(10, 60));
   float size;

   public FadingCircle(float x0, float y0, color c0, float size0){
      x = x0;
      y = y0;
      c = c0;
      size = size0;
   }

   void display(){
      float transparency = 255.0 * (frames / 20.0);
      // fill(c, transparency);
      // noStroke();
      stroke(c, transparency);
      noFill();
      ellipse(x, y, size, size);
      frames -= 1.0;
   }
}


void draw_line(){
   for (int i = 0; i < orbs.size(); i++) {
      Orb current_orb = orbs.get(i);
      for (int j = 0; j < orbs.size(); j++) {
         Orb test_orb = orbs.get(j);
         if (distance(current_orb.xpos, current_orb.ypos, test_orb.xpos, test_orb.ypos) < (current_orb.orbsize/2.0 + test_orb.orbsize/2.0)){
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
   float speed = 0.5;
   public Orb(float x, float y){
      orbsize = random(1,2) * height/50.0;
      x_speed = random(-speed, speed);
      y_speed = random(-speed, speed);
      xpos = x;
      ypos = y;
   }
   
   void display(){
      noStroke();
      fill(0);
      ellipse(xpos, ypos, orbsize, orbsize);
   }
   
   void move(){
      if (xpos + x_speed + orbsize/2 > width || xpos + x_speed - orbsize/2 < 0){
         x_speed *= -1;
      }
      if (ypos + y_speed + orbsize/2 > height || ypos + y_speed - orbsize/2 < 0){
         y_speed *= -1;
      }
      xpos = xpos + x_speed;
      ypos = ypos + y_speed;
   } 
}

float prop(float value){
   return value * width / 512;
}

float distance(float x1, float y1, float x2, float y2){
   return (float)sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
}