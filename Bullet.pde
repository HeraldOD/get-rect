class Bullet {
  PVector pos, vel;
  float r, grav;
  boolean dead;
  float FH = height - 4*height/5; // floor height from bottom

  Bullet(float x, float y, float dx, float dy) {
    pos = new PVector(x, y);
    vel = new PVector(dx, dy);
    r = EYE_SIZE;
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
    noStroke();
    fill(0);
    rect(pos.x, pos.y, r, r);
  }
}
