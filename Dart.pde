class Dart {
  PVector pos, vel;
  float r, grav;
  PImage sprite;
  boolean dead;
  
  Dart(float x, float y, float dx, float dy) {
    pos = new PVector(x, y);
    vel = new PVector(dx, dy);
    r = PLAYER_SIZE/2;
    sprite = dart;
    grav = BULLET_GRAVITY;
    dead = false;
  }
  
  void update() {
    pos.add(vel);
    vel.y += grav;
    
    if (pos.x > width - r || pos.x < 0 || pos.y < -r || pos.y > 4*height/5 - r
      || (pos.x + r > platformX && pos.x < platformX + width/2 && pos.y + r > platformY && pos.y < platformY + FLOOR_WIDTH/4) ) {
      dead = true;
    }
  }
  
  void show() {
    image(dart, pos.x, pos.y, r, r);
  }
  
}
