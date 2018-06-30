class Rocket {
  PVector pos, vel;
  float r;
  boolean facingR, dead;
  PImage sprite;

  Rocket(float x, float y, boolean faceR) {
    facingR = faceR;
    pos = new PVector(x, y);
    vel = new PVector(0, 0);
    sprite = rocket;
    dead = false;

    r = PLAYER_SIZE/2;
  }

  void update() {

    if (facingR) {
      vel.x = lerp(vel.x, ROCKET_SPEED, 0.15);
    } else {
      vel.x = lerp(vel.x, -ROCKET_SPEED, 0.15);
    }
    
    pos.add(vel);

    if (pos.x + r >= width) {
      pos.x = width - r;
      dead = true;
    }
    if (pos.x <= 0) {
      pos.x = 0;
      dead = true;
    }
  }

  void show() {
    image(sprite, pos.x, pos.y, r, r);
    spawnSmoke(pos.x + r/2, pos.y + r/2);
  }
}
