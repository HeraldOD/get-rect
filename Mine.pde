class Mine {
  PVector pos;
  float r;
  boolean dead;
  PImage sprite;
  
  Mine(float x, float y) {
    pos = new PVector(x, y);
    r = PLAYER_SIZE;
    dead = false;
    sprite = mine;
  }
  
  void update() {
    for (int i = 0; i < bullets.size(); i++) { // CHECK IF HIT BY BULLET
      Bullet b = bullets.get(i);
      if (b.pos.x + EYE_SIZE > pos.x && b.pos.x < pos.x + r && b.pos.y + EYE_SIZE > pos.y && b.pos.y < pos.y + EYE_SIZE) {
        dead = true;
        bullets.remove(b);
      }
    }
  }
  
  void show() {
    stroke(0);
    fill(0, 80, 0);
    image(mine, pos.x, pos.y, r, EYE_SIZE);
  }
}
