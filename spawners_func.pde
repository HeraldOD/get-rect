void spawnPowerup() {
  float x, y;
  int type;

  boolean onPlatform = int(random(3)) < 1;

  if (onPlatform) {
    x = random(width/4, width/4 + width/2 - PLAYER_SIZE);
    y = platformY - PLAYER_SIZE - 5;
  } else {
    x = random(width - PLAYER_SIZE);
    y = 4*height/5 - PLAYER_SIZE - 5;
  }

  type = int(random(10) + 1);

  powpop.trigger();
  Powerup powerup = new Powerup(x, y, type);
  powerups.add(powerup);
}

void spawnBullet(float posx, float posy, boolean facingR, float num) {
  float x = posx;
  float y = posy;

  float dx;

  for (int i = 0; i < num; i++) {
    if (facingR) {
      dx = random(BULLET_SPEED - 2, BULLET_SPEED + 2);
    } else {
      dx = -random(BULLET_SPEED - 2, BULLET_SPEED + 2);
    }
    Bullet bullet = new Bullet(x, y, dx, num*random(-0.2, 0.2));
    bullets.add(bullet);
  }
}

void bulletBurst(float x, float y, float num, float pow) {
  for (int i = 0; i < num; i++) {
    float a = random(TWO_PI);
    float dx = pow*cos(a);
    float dy = pow*sin(a);
    Bullet bullet = new Bullet(x - EYE_SIZE/2, y - EYE_SIZE/2, dx, dy);
    bullets.add(bullet);
  }
  for (int i = 0; i < num*2; i++) {
    float a = random(TWO_PI);
    float dx = 4*cos(a);
    float dy = 4*sin(a);
    spawnFire(x, y, dx, dy);
  }
}

void spawnSmoke(float x, float y) {
  Particle p = new Particle(x, y, random(-0.1, 0.1), random(-0.9, -1.1), random(8, 16), color(100, 100), 150, 0, false);
  particles.add(p);
}

void spawnFire(float x, float y, float dx, float dy) {
  color c;
  float sz;
  if (random(3) < 1) {
    c = color(200, 10, 0, 100);
    sz = 8;
  } else {
    c = color(255, 195, 28, 100);
    sz = 14;
  }
  Particle p = new Particle(x, y, dx, dy, sz, c, 80, FIRE_DMG, true);
  particles.add(p);
}

void laserBeam(float x, float y, boolean facingR) {
  if (facingR) {
    for (int i = int(x); i < width; i += 2) {
      Particle p = new Particle(i, y, 0, 0, random(4, 10), color(255, 0, 0, 100), 25, 1, true);
      particles.add(p);
    }
  } else {
    for (int i = int(x); i > 0; i -= 2) {
      Particle p = new Particle(i, y, 0, 0, random(4, 10), color(255, 0, 0, 100), 25, 1, true);
      particles.add(p);
    }
  }
  
  Particle p = new Particle(x, y, 0, 0, 200, color(255, 0, 0, 100), 20, 0, false);
  particles.add(p);
}
