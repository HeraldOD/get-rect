class Trampoline {
  PVector pos;
  float r, life, lifetime;
  boolean dead;
  PImage sprite;
  
  Trampoline(float x, float y) {
    pos = new PVector(x, y);
    r = PLAYER_SIZE;
    life = 0;
    lifetime = TRAMP_DURATION;
    dead = false;
    sprite = tramp;
  }
  
  void update() {
    life ++;
    
    if (life >= lifetime) {
      dead = true;
    }
  }
  
  void show() {
    stroke(0);
    fill(0, 80, 0);
    image(tramp, pos.x, pos.y, r, EYE_SIZE);
  }
}
