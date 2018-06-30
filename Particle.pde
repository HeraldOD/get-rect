class Particle {
  PVector pos, vel;
  float r, initialR, life, lifetime, damage;
  color c;
  boolean dead, touchable;

  Particle(float x, float y, float dx, float dy, float sz, color col, float lifet, float dmg, boolean touch) {
    pos = new PVector(x, y);
    vel = new PVector(dx, dy);
    r = sz;
    initialR = sz;
    c = col;
    life = 0;
    lifetime = lifet;
    damage = dmg;
    touchable = touch;
  }

  void update() {
    life ++;

    r -= initialR / lifetime;

    if (life >= lifetime) {
      dead = true;
    }

    pos.add(vel);
  }

  void show() {
    noStroke();
    fill(c);
    rectMode(CENTER);
    rect(pos.x, pos.y, r, r);
    rectMode(CORNER);
  }
}
