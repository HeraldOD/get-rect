class Player {
  PVector pos, old, vel;
  float r, grav, fric, speed, up, down, left, right, block, jumpPow, eyeSz, Scooldown, Pcooldown, health, Ptype, shootC, powC, Bcooldown, blockC, blocking, barX, barY, poison, mvt, 
    lerpHealth;
  boolean facingR, dead, falling;
  color col;
  float FH = height - 4*height/5; // floor height from bottom

  Player(float x, float y, boolean fr) {
    pos = new PVector(x, y);
    old = new PVector(x-1, y-1);
    vel = new PVector(0, 0);
    facingR = fr;
    r = PLAYER_SIZE;
    grav = PLAYER_GRAVITY;
    fric = 0.9;
    up = 0;
    down = 0;
    left = 0;
    right = 0;
    block = 0;
    speed = MOVE_SPEED;
    jumpPow = JUMP_POW;
    eyeSz = EYE_SIZE;
    Scooldown = INITIAL_COOLDOWN;
    health = PLAYER_HEALTH;
    lerpHealth = health;
    dead = false;
    falling = true;
    Ptype = 0;
    Pcooldown = 0;
    Bcooldown = 0;
    blockC = BLOCK_COOLDOWN;
    shootC = SHOOT_COOLDOWN;
    powC = POWERUP_COOLDOWN;
    blocking = 0;
    barX = pos.x;
    barY = pos.y;
    poison = 0;
    mvt = 0;
  }


  void update() { // =============================== UPDATE
    old = pos;

    if (poison > 0) {
      health -= POISON_DMG;
      poison --;
    }

    health += REGEN_RATE;

    if (Ptype == 1 || Ptype == 10) { // MACHINEGUN + DART
      shootC = 8;
    } else if (Ptype == 3) { // BURST
      shootC = 2.5*SHOOT_COOLDOWN;
    } else if (Ptype == 5) { // GRENADE
      shootC = 2*SHOOT_COOLDOWN;
    } else if (Ptype == 6) { // BAZOOKA
      shootC = 3*SHOOT_COOLDOWN;
    } else if (Ptype == 7) { // LASER
      shootC = 2*SHOOT_COOLDOWN;
    } else if (Ptype == 8 || Ptype == 9) { // TRAMP + MINE
      shootC = 3.5*SHOOT_COOLDOWN;
    } else {
      shootC = SHOOT_COOLDOWN;
    }

    if (health > PLAYER_HEALTH) {
      health = PLAYER_HEALTH;
    }

    for (int i = 0; i < bullets.size(); i++) { // CHECK IF HIT BY BULLET
      Bullet b = bullets.get(i);
      if (b.pos.x + EYE_SIZE > pos.x && b.pos.x < pos.x + r && b.pos.y + EYE_SIZE > pos.y && b.pos.y < pos.y + r) {

        if (blocking <= 0) {
          hit.trigger();
          health -= BULLET_DAMAGE;
        }

        bullets.remove(b);
      }
    }

    for (int i = 0; i < particles.size(); i++) { // CHECK IF HIT BY PARTICLE
      Particle p = particles.get(i);
      if (p.pos.x + p.r > pos.x && p.pos.x < pos.x + r && p.pos.y + p.r > pos.y && p.pos.y < pos.y + r) {

        if (blocking <= 0) {
          health -= p.damage;
        }
        
        if (p.touchable) {
          particles.remove(p);
        }
      }
    }

    for (int i = 0; i < rockets.size(); i++) { // CHECK IF HIT BY ROCKET
      Rocket p = rockets.get(i);
      if (p.pos.x + p.r > pos.x && p.pos.x < pos.x + r && p.pos.y + p.r > pos.y && p.pos.y < pos.y + r) {
        p.dead = true;
      }
    }

    for (int i = 0; i < darts.size(); i++) { // CHECK IF HIT BY DART
      Dart d = darts.get(i);
      if (d.pos.x + d.r > pos.x && d.pos.x < pos.x + r && d.pos.y + d.r > pos.y && d.pos.y < pos.y + r) {
        hit.trigger();
        d.dead = true;
        poison = POISON_TIME;
      }
    }

    for (int i = 0; i < tramps.size(); i++) { // CHECK IF TOUCHING TRAMPOLINE
      Trampoline t = tramps.get(i);
      if (t.pos.x + t.r > pos.x && t.pos.x < pos.x + r && t.pos.y + EYE_SIZE > pos.y && t.pos.y < pos.y + r) {
        boing.trigger();
        vel.y = -2*JUMP_POW;
      }
    }

    for (int i = 0; i < mines.size(); i++) { // CHECK IF TOUCHING MINE
      Mine m = mines.get(i);
      if (m.pos.x + m.r > pos.x && m.pos.x < pos.x + r && m.pos.y + EYE_SIZE > pos.y && m.pos.y < pos.y + r) {
        m.dead = true;
        vel.y += -10;
      }
    }

    for (int i = 0; i < powerups.size(); i++) { // CHECK IF TOUCHING POWERUP
      Powerup p = powerups.get(i);
      if (p.pos.x + p.r > pos.x && p.pos.x < pos.x + r && p.pos.y + p.r > pos.y && p.pos.y < pos.y + r) {

        if (p.type == 4) {
          heal.trigger();
          health += PLAYER_HEALTH/4;
        } else {
          Ptype = p.type;

          if (Ptype == 1 || Ptype == 3 || Ptype == 6 || Ptype == 5) {
            reload.trigger();
          }
          if (Ptype == 7) {
            laserequip.trigger();
          }

          Pcooldown = POWERUP_DURATION;
        }
        powerups.remove(p);
      }
    }
    
    lerpHealth = lerp(lerpHealth, health, 0.2);
    if (health < 0) {
      die.trigger();
      health = 0;
      dead = true;
    }

    if (Scooldown > 0) {
      Scooldown --;
    }
    if (Pcooldown > 0) {
      Pcooldown --;
    }
    if (Bcooldown > 0) {
      Bcooldown --;
    }
    if (blocking > 0) {
      blocking --;
    }

    if (Pcooldown == 0) {
      Ptype = 0;
    }

    if (left == 1) { 
      facingR = false;
    }
    if (right == 1) { 
      facingR = true;
    }

    if (block == 1 && Bcooldown <= 0) { // BLOCK
      blocking = BLOCK_DURATION;
      Bcooldown = BLOCK_COOLDOWN;
    }

    if (up == 1) {
      if (Ptype == 2) { // JETPACK FLY
        fly.trigger();
        vel.y = lerp(vel.y, -MOVE_SPEED*1.2, 0.2);
      } else {
        if ( pos.y + r >= 4*height/5 || (pos.x + r >= platformX && pos.x <= platformX + width/2 && pos.y + r >= platformY && pos.y + r < platformY + FLOOR_WIDTH/4) ) { // JUMP
          boing.trigger();
          vel.y = -jumpPow;
        }
      }
    }

    if (pos.x <= 0) { // LEFT WALL JUMP
      vel.y = (1-left)*WALL_SPEED;
      falling = false;

      if (up == 1) {
        vel.y = -jumpPow;
        vel.x = 2*jumpPow;
        boing.trigger();
      }
    } else if (pos.x >= width-r) { // RIGHT WALL JUMP
      vel.y = (1-right)*WALL_SPEED;
      falling = false;

      if (up == 1) {
        boing.trigger();
        vel.y = -jumpPow;
        vel.x = -2*jumpPow;
      }
    } else {
      falling = true;
    }

    if (down == 1 && Scooldown <= 0) { // SHOOT

      if (Ptype == 1) {
        machinegun.trigger();
      } else if (Ptype == 3) {
        burst.trigger();
      } else if (Ptype == 5 || Ptype == 8 || Ptype == 9 || Ptype == 10) {
        gthrow.trigger();
      } else if (Ptype == 6) {
        rocketshot.trigger();
      } else if (Ptype == 7) {
        lasershot.trigger();
      } else {
        shoot.trigger();
      }

      float num;
      if (Ptype == 3) {
        num = BULLET_NUMBER*5;
      } else if (Ptype == 1) {
        num = 1;
      } else {
        num = BULLET_NUMBER;
      }

      if (facingR) { // RIGHT SHOT
        if (Ptype == 5) {
          Grenade g = new Grenade(pos.x + r, pos.y, THROW_POW, -THROW_POW/2);
          grenades.add(g);
        } else if (Ptype == 6) {
          Rocket rocket = new Rocket(pos.x + r, pos.y, true);
          rockets.add(rocket);
          vel.x += -ROCKET_RECOIL;
        } else if (Ptype == 7) {
          laserBeam(pos.x + r + EYE_SIZE, pos.y + r/2, true);
          vel.x += -LASER_RECOIL;
        } else if (Ptype == 8) {
          Trampoline t = new Trampoline(pos.x, pos.y + r - EYE_SIZE);
          tramps.add(t);
        } else if (Ptype == 9) {
          Mine m = new Mine(pos.x + r, pos.y + r - EYE_SIZE);
          mines.add(m);
          vel.x += -5;
          vel.y += -5;
        } else if (Ptype == 10) {
          Dart d = new Dart(pos.x + r, pos.y + r/4, DART_SPEED, 0);
          darts.add(d);
        } else {
          spawnBullet(pos.x + r, pos.y + r/2, true, num);
        }
      } else { // LEFT SHOT
        if (Ptype == 5) {
          Grenade g = new Grenade(pos.x - r/2, pos.y, -THROW_POW, -THROW_POW/2);
          grenades.add(g);
        } else if (Ptype == 6) {
          Rocket rocket = new Rocket(pos.x - r/2, pos.y, false);
          rockets.add(rocket);
          vel.x += ROCKET_RECOIL;
        } else if (Ptype == 7) {
          laserBeam(pos.x + r + EYE_SIZE, pos.y + r/2, false);
          vel.x += LASER_RECOIL;
        } else if (Ptype == 8) {
          Trampoline t = new Trampoline(pos.x, pos.y + r - EYE_SIZE);
          tramps.add(t);
        } else if (Ptype == 9) {
          Mine m = new Mine(pos.x - r, pos.y + r - EYE_SIZE);
          mines.add(m);
          vel.x += 5;
          vel.y += -5;
        } else if (Ptype == 10) {
          Dart d = new Dart(pos.x - r/2, pos.y + r/4, -DART_SPEED, 0);
          darts.add(d);
        } else {
          spawnBullet(pos.x - eyeSz, pos.y + r/2, false, num);
        }
      }

      Scooldown = shootC;
    }
    
    mvt = lerp(mvt, (right - left) * speed, 0.3);
    pos.x += mvt; // KEY MOVEMENT
    pos.add(vel);

    if (pos.x + r >= platformX && pos.x <= platformX + width/2 && pos.y + r >= platformY && pos.y + r <= platformY + FLOOR_WIDTH/4) { // v PLATFORM COLLISION v

      falling = false;

      pos.y = platformY - r;

      vel.y = 0;
    }

    if (pos.y + r > 4*height/5) { // v OUT OF BOUNDS HANDLING v
      pos.y = 4*height/5 - r;
      falling = false;
      vel.y = 0;
    }
    if (pos.y < 0) {
      pos.y = 0;
      if (vel.y < 0) {
        vel.y = 0;
      }
    }
    if (pos.x + r > width) {
      pos.x = width-r;
      vel.x = 0;
    }
    if (pos.x < 0) {
      pos.x = 0;
      vel.x = 0;
    }

    if (pos.y == 0 && up == 1) { // GRAB ONTO CEILING
      falling = false;
    }

    if (falling == true) { 
      vel.y += grav;
    } // GRAVITY
    vel.x *= fric; // FRICTION
  }

  // DRAW ============================================================================================================================================================
  void show(color c) {
    col = c;
    stroke(0); // DRAW BODY
    fill(c);
    rect(pos.x, pos.y, r, r);

    if (!dead) {
      stroke(0); // DRAW EYE
      fill(255);
      if (facingR) { // RIGHT >>>>>>>>>>>>>>>
        if (Ptype == 2) {
          if (up == 1) {
            spawnFire(pos.x - r/4, pos.y + r, random(-0.1, 0.1), 1);
          }
          image(jetpack, pos.x - PLAYER_SIZE/2, pos.y, PLAYER_SIZE/2, PLAYER_SIZE);
        } else if (Ptype > 0) {
          image(Psprites[int(Ptype)], pos.x, pos.y - r/6, r, r);
        }

        rect(pos.x + r - eyeSz, pos.y + r/2, eyeSz, eyeSz);
      } else { // LEFT <<<<<<<<<<<<<<<<<<<<<<
        if (Ptype == 2) {
          if (up == 1) {
            spawnFire(pos.x + r + r/4, pos.y + r, random(-0.1, 0.1), 1);
          }
          image(jetpack, pos.x + PLAYER_SIZE, pos.y, PLAYER_SIZE/2, PLAYER_SIZE);
        } else if (Ptype > 0) {
          image(Psprites[int(Ptype)], pos.x, pos.y - r/6, r, r);
        }

        rect(pos.x, pos.y + r/2, eyeSz, eyeSz);
      }

      if (blocking > 0) {
        image(shield, pos.x - PLAYER_SIZE/10, pos.y - PLAYER_SIZE/10, PLAYER_SIZE + PLAYER_SIZE/5, PLAYER_SIZE + PLAYER_SIZE/5);
      }

      barX = lerp(barX, pos.x, 0.2);
      barY = lerp(barY, pos.y, 0.2);

      if (barX < 10) { // BARS OUT OF BOUNDS CORRECTION
        barX = 10;
      }
      if (barX > width - (r + 10) ) {
        barX = width - (r + 10);
      }
      if (Ptype > 0) {
        if (barY < 26) {
          barY = 26;
        }
      } else {
        if (barY < 16) {
          barY = 16;
        }
      }
      
      noStroke(); // DRAW HEALTH BAR (+ POWERUP BAR)
      if (lerpHealth > PLAYER_HEALTH/2) {
        fill(0, 200, 0, 180);
      } else if (lerpHealth > PLAYER_HEALTH/4) {
        fill(255, 153, 0, 180);
      } else {
        fill(200, 0, 0, 180);
      }
      float bar = map(lerpHealth, 0, PLAYER_HEALTH, 0, r + 20);
      rect(barX - 10, barY - 16, bar, 12);

      noFill();
      if (poison > 0) {
        stroke(#AD00F2);
        strokeWeight(3);
      } else {
        stroke(0);
        strokeWeight(2);
      }
      rect(barX - 10, barY - 16, r + 20, 12);
      strokeWeight(2);

      if (Ptype > 0) { // POWERUP BAR
        noStroke();
        fill(150, 30, 0, 180);
        bar = map(Pcooldown, 0, POWERUP_DURATION, 0, r + 20);
        rect(barX - 10, barY - 26, bar, 6);

        noFill();
        stroke(0);
        rect(barX - 10, barY - 26, r + 20, 6);
      }
    } else {
      stroke(0); // DRAW DEAD EYE
      strokeWeight(3);
      if (facingR) {
        line(pos.x + r - eyeSz, pos.y + r/2, pos.x + r, pos.y + r/2 + eyeSz);
        line(pos.x + r - eyeSz, pos.y + r/2 + eyeSz, pos.x + r, pos.y + r/2);
      } else {
        line(pos.x, pos.y + r/2, pos.x + eyeSz, pos.y + r/2 + eyeSz);
        line(pos.x, pos.y + r/2 + eyeSz, pos.x + eyeSz, pos.y + r/2);
      }
      strokeWeight(2);
    }
  }
}
