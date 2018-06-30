class Grenade {
  PVector pos, vel;
  float r, grav, lifetime, life, bounce, fric;
  boolean dead;

  Grenade(float x, float y, float dx, float dy) {
    pos = new PVector(x, y);
    vel = new PVector(dx, dy);

    dead = false;

    r = PLAYER_SIZE;
    grav = PLAYER_GRAVITY;

    lifetime = GRENADE_TIME;
    life = 0;

    bounce = GRENADE_BOUNCE;

    fric = 0.9;
  }

  void update() {
    life ++;

    if (life >= lifetime) {
      dead = true;
    }

    for (int i = 0; i < bullets.size(); i++) { // CHECK IF HIT BY BULLET
      Bullet b = bullets.get(i);
      if (b.pos.x + EYE_SIZE > pos.x && b.pos.x < pos.x + r && b.pos.y + EYE_SIZE > pos.y && b.pos.y < pos.y + r) {
        dead = true;
        bullets.remove(b);
      }
    }

    pos.add(vel);

    if (pos.x + r >= platformX && pos.x <= platformX + width/2 && pos.y + r >= platformY && pos.y <= platformY + FLOOR_WIDTH/4) { // v PLATFORM COLLISION v
      pos.y = platformY - r;
      vel.y = -vel.y*bounce;
      vel.x *= fric;
    }

    if (pos.y + r >= 4*height/5) { // v OUT OF BOUNDS HANDLING v
      pos.y = 4*height/5 - r;
      vel.y = -vel.y*bounce;
      vel.x *= fric;
    }
    if (pos.y <= 0) {
      pos.y = 0;
      vel.y = -vel.y*bounce;
    }
    if (pos.x + r >= width) {
      pos.x = width - r;
      vel.x = -vel.x;
    }
    if (pos.x <= 0) {
      pos.x = 0;
      vel.x = -vel.x;
    }

    vel.y += grav;

    spawnSmoke(pos.x + r/2, pos.y);
  }

  void show() {
    stroke(0);
    fill(0, 120, 0);
    image(grenade, pos.x, pos.y, r, r);
  }
}
