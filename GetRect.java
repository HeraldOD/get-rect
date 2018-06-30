import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ddf.minim.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class GetRect extends PApplet {



Minim minim;
AudioSample boing, shoot, hit, die, fly, machinegun, burst, heal, reload, gthrow, explode, powpop, rocketshot, lasershot, laserequip;

Player p1, p2;

ArrayList <Bullet> bullets;
ArrayList <Powerup> powerups;
ArrayList <Grenade> grenades;
ArrayList <Particle> particles;
ArrayList <Rocket> rockets;
ArrayList <Trampoline> tramps;
ArrayList <Mine> mines;
ArrayList <Dart> darts;

PImage bg, jetpack, grenade, rocket, shield, tramp, mine, spriteBG, dart;
PImage[] Psprites = new PImage[11];

// SETTINGS ==================================================================================
int P1Color = color(180, 50, 50);
int P2Color = color(50, 50, 180);
int floorColor = color(180);

float INITIAL_COOLDOWN = 80; // Time at beginning of game before anyone can shoot ; Frames
float PLAYER_SIZE = 40; // Size of players and powerups ; Pixels
float PLAYER_HEALTH = 100; // Arbitrary unit
float BULLET_DAMAGE = 10; // Self explanatory
float REGEN_RATE = 0.03f; // Health per frame regained by both players
float EYE_SIZE = PLAYER_SIZE/4; // Size of eyes & bullets + height of trampolines & mines ; Pixels
float MOVE_SPEED = 8; // Speed of players ; Pixels per second
float WALL_SPEED = 2; // Speed of players while on wall ; Pixels per second
float JUMP_POW = 12; // Player jump power ; Vertical velocity (pixels per second) ; Also affects wall jump power
float THROW_POW = 13; // Player grenade throw power ; Horizontal velocity
float BULLET_SPEED = 18; // Speed of bullets ; Pixels per second
float BULLET_NUMBER = 1; // Number of bullets in a normal shot
float PLAYER_GRAVITY = 0.45f; // Gravity of player ; Pixels down added to acceleration per second
float BULLET_GRAVITY = 0; // Gravity of bullets ; Pixels down added to acceleration per second
float SHOOT_COOLDOWN = 20; // Time, per player, between each shot ; Frames
float POWERUP_COOLDOWN = 500; // Time bewteen each powerup spawn ; Frames 
float POWERUP_DURATION = 450; // Time a powerup lasts once picked up ; Frames
float EVENT_COOLDOWN = 1300; // Time between each event ; Frames
float EVENT_SIZE = 50; // Number of bullets shot from above
float FLOOR_WIDTH = 70; // Thickness of floor platform (platform scales) ; Pixels
float GRENADE_TIME = 120; // Time before a grenade detonates ; Frames
float GRENADE_BOUNCE = 0.7f; // Amoun of bounciness ; Proportion of last bounce to current bounce
float FIRE_DMG = 2; // Damage done by fire to players ; Damage per frame
float ROCKET_SPEED = 16; // Speed of bazooka rockets ; Pixels per second
float BLOCK_COOLDOWN = 80; // Time between each block ; Frames
float BLOCK_DURATION = 25; // Time a block lasts ; Frames
float ROCKET_RECOIL = 21; // Recoil of bazooka powerup on shoot ; Vertical velocity
float LASER_RECOIL = 15; // Recoil of laser powerup on shoot ; Vertical velocity
float TRAMP_DURATION = 1600; // Duration of a trampoline before it disappears ; Frames
float POISON_DMG = 0.2f; // Damage dealt when poisoned ; Damage per frame
float POISON_TIME = 300; // Duration of poison ; Frames
float DART_SPEED = 11;

float platformX;
float platformY;

// ===========================================================================================

public void reset() {
  p1 = new Player(100, 100, false);
  p2 = new Player(width - 100 - PLAYER_SIZE, 100, true);
  bullets = new ArrayList<Bullet>(0);
  powerups = new ArrayList <Powerup>(0);
  grenades = new ArrayList <Grenade>(0);
  particles = new ArrayList <Particle>(0);
  rockets = new ArrayList <Rocket>(0);
  tramps = new ArrayList <Trampoline>(0);
  mines = new ArrayList <Mine>(0);
  darts = new ArrayList <Dart>(0);
}

public void event() {
  for (int i = 0; i < EVENT_SIZE; i++) {
    float x = random(10, width - 10);
    Bullet bullet = new Bullet(x, 1, random(-2, 2), random(3, 8));
    bullets.add(bullet);
  }
}

// SETUP +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public void setup() {
  
  //size(1200, 800, P2D); // SET RESOLUTION HERE
  frameRate(60);
  noCursor();

  strokeWeight(2);

  minim = new Minim(this);

  gthrow = minim.loadSample("gthrow.mp3", 128);
  boing = minim.loadSample("boing.mp3", 128);
  shoot = minim.loadSample("shoot.mp3", 128);
  hit = minim.loadSample("hit.mp3", 128);
  die = minim.loadSample("die.mp3", 128);
  fly = minim.loadSample("fly.mp3", 128);
  machinegun = minim.loadSample("machinegun.mp3", 128);
  burst = minim.loadSample("burst.mp3", 128);
  reload = minim.loadSample("reload.mp3", 128);
  heal = minim.loadSample("heal.mp3", 128);
  explode = minim.loadSample("explode.mp3", 128);
  powpop = minim.loadSample("powerup-pop.mp3", 128);
  rocketshot = minim.loadSample("rocket.mp3", 128);
  lasershot = minim.loadSample("lasershot.mp3", 128);
  laserequip = minim.loadSample("laserequip.mp3", 128);

  bg = loadImage("bg.png");
  jetpack = loadImage("jetpack.png");
  grenade = loadImage("grenade.png");
  rocket = loadImage("rocket.png");
  shield = loadImage("shield.png");
  tramp = loadImage("tramp.png");
  mine = loadImage("mine.png");
  dart = loadImage("dart.png");
  
  spriteBG = loadImage("pow-bg.png");
  Psprites[0] = loadImage("null.png");
  Psprites[1] = loadImage("pow-machinegun.png");
  Psprites[2] = loadImage("pow-jetpack.png");
  Psprites[3] = loadImage("pow-burst.png");
  Psprites[4] = loadImage("pow-heal.png");
  Psprites[5] = loadImage("pow-grenade.png");
  Psprites[6] = loadImage("pow-bazooka.png");
  Psprites[7] = loadImage("pow-lasergun.png");
  Psprites[8] = loadImage("pow-tramp.png");
  Psprites[9] = loadImage("pow-mine.png");
  Psprites[10] = loadImage("pow-dart.png");

  platformX = width/4;
  platformY = height/2 - FLOOR_WIDTH/2;

  reset();
}


// DRAW ======================================================================================================================================================================================
public void draw() {
  image(bg, 0, 0);

  //debug();

  if (p1.dead) {
    p2.health = PLAYER_HEALTH;
    textSize(50);
    fill(0);
    text("PLAYER 2 WINS", width/2 - 185, 200);
  }
  if (p2.dead) {
    p1.health = PLAYER_HEALTH;
    textSize(50);
    fill(0);
    text("PLAYER 1 WINS", width/2 - 185, 200);
  }


  stroke(0);
  fill(floorColor);
  rect(-1, 4*height/5, width + 2, FLOOR_WIDTH); // FLOOR
  rect(platformX, platformY, width/2, FLOOR_WIDTH/4); // PLATFORM

  noStroke();
  fill(130, 130, 130);
  float prog = map(frameCount % EVENT_COOLDOWN, 0, EVENT_COOLDOWN - 1, 0, width);
  rect(0, 4*height/5 + FLOOR_WIDTH/5, prog, FLOOR_WIDTH - 2*(FLOOR_WIDTH/5)); // EVENT PROGRESS BAR

  if (frameCount % EVENT_COOLDOWN == 0) { // TRIGGER EVENT
    event();
  }
  if (frameCount % POWERUP_COOLDOWN == 0) { // SPAWN POWERUP
    spawnPowerup();
  }


  for (Bullet b : bullets) { // DRAW ALL BULLETS
    b.update();
    b.show();
  }
  for (Powerup p : powerups) { // DRAW ALL POWERUPS
    p.update();
    p.show();
  }
  for (Grenade g : grenades) { // DRAW ALL GRENADES
    g.update();
    g.show();
  }
  for (Particle p : particles) { // DRAW ALL PARTICLES
    p.update();
    p.show();
  }
  for (Rocket r : rockets) { // DRAW ALL ROCKETS
    r.update();
    r.show();
  }
  for (Dart d : darts) { // DRAW ALL DARTS
    d.update();
    d.show();
  }


  if (!p1.dead) { // DRAW P1 IF NOT DEAD
    p1.update();
  }
  p1.show(P1Color);
  if (!p2.dead) { // DRAW P2 IF NOT DEAD
    p2.update();
  }
  p2.show(P2Color);
  
  for (Trampoline t : tramps) { // DRAW ALL TRAMPOLINES
    t.update();
    t.show();
  }
  for (Mine m : mines) { // DRAW ALL MINES
    m.update();
    m.show();
  }
  
  
  // DESTRUCTION **************************************************************
  for (int i = 0; i < bullets.size(); i++) { // DESTROY OUT OF BOUNDS BULLETS
    Bullet b = bullets.get(i);
    if (b.dead) {
      bullets.remove(b);
    }
  }

  for (int i = 0; i < grenades.size(); i++) { // EXPLODE OUT OF BOUNDS GRENADES
    Grenade g = grenades.get(i);
    if (g.dead) {
      explode.trigger();
      bulletBurst(g.pos.x + PLAYER_SIZE/4, g.pos.y + PLAYER_SIZE/4, 20, 10);
      grenades.remove(grenades.get(i));
    }
  }

  for (int i = 0; i < rockets.size(); i++) { // EXPLODE OUT OF BOUNDS ROCKETS
    Rocket r = rockets.get(i);
    if (r.dead) {
      explode.trigger();
      bulletBurst(r.pos.x + r.r/2, r.pos.y + r.r/2, 25, 12);
      rockets.remove(rockets.get(i));
    }
  }

  for (int i = 0; i < particles.size(); i++) { // REMOVE DEAD PARTICLES
    Particle p = particles.get(i);
    if (p.dead) {
      particles.remove(particles.get(i));
    }
  }
  
  for (int i = 0; i < tramps.size(); i++) { // REMOVE DEAD TRAMPS
    Trampoline t = tramps.get(i);
    if (t.dead) {
      tramps.remove(tramps.get(i));
    }
  }
  
  for (int i = 0; i < darts.size(); i++) { // REMOVE DEAD DARTS
    Dart d = darts.get(i);
    if (d.dead) {
      darts.remove(darts.get(i));
    }
  }
  
  for (int i = 0; i < mines.size(); i++) { // EXPLODE DEAD MINES
    Mine m = mines.get(i);
    if (m.dead) {
      explode.trigger();
      bulletBurst(m.pos.x + PLAYER_SIZE/2, m.pos.y + EYE_SIZE/2, 10, 12);
      mines.remove(mines.get(i));
    }
  }
}



//void mousePressed() {
//  laserBeam(mouseX, mouseY, false);
//}


public void stop() {
  boing.close();
  shoot.close();
  hit.close();
  die.close();
  fly.close();
  machinegun.close();
  burst.close();
  heal.close();
  reload.close();
  gthrow.close();
  explode.close();
  powpop.close();
  rocketshot.close();
  lasershot.close();
  laserequip.close();

  minim.stop();
  super.stop();
}
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

  public void update() {
    pos.add(vel);
    vel.y += grav;

    if (pos.x > width - r || pos.x < 0 || pos.y < -r || pos.y > 4*height/5 - r
      || (pos.x + r > platformX && pos.x < platformX + width/2 && pos.y + r > platformY && pos.y < platformY + FLOOR_WIDTH/4) ) {
      dead = true;
    }
  }

  public void show() {
    noStroke();
    fill(0);
    rect(pos.x, pos.y, r, r);
  }
}
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
  
  public void update() {
    pos.add(vel);
    vel.y += grav;
    
    if (pos.x > width - r || pos.x < 0 || pos.y < -r || pos.y > 4*height/5 - r
      || (pos.x + r > platformX && pos.x < platformX + width/2 && pos.y + r > platformY && pos.y < platformY + FLOOR_WIDTH/4) ) {
      dead = true;
    }
  }
  
  public void show() {
    image(dart, pos.x, pos.y, r, r);
  }
  
}
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

    fric = 0.9f;
  }

  public void update() {
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

  public void show() {
    stroke(0);
    fill(0, 120, 0);
    image(grenade, pos.x, pos.y, r, r);
  }
}
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
  
  public void update() {
    for (int i = 0; i < bullets.size(); i++) { // CHECK IF HIT BY BULLET
      Bullet b = bullets.get(i);
      if (b.pos.x + EYE_SIZE > pos.x && b.pos.x < pos.x + r && b.pos.y + EYE_SIZE > pos.y && b.pos.y < pos.y + EYE_SIZE) {
        dead = true;
        bullets.remove(b);
      }
    }
  }
  
  public void show() {
    stroke(0);
    fill(0, 80, 0);
    image(mine, pos.x, pos.y, r, EYE_SIZE);
  }
}
class Particle {
  PVector pos, vel;
  float r, initialR, life, lifetime, damage;
  int c;
  boolean dead, touchable;

  Particle(float x, float y, float dx, float dy, float sz, int col, float lifet, float dmg, boolean touch) {
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

  public void update() {
    life ++;

    r -= initialR / lifetime;

    if (life >= lifetime) {
      dead = true;
    }

    pos.add(vel);
  }

  public void show() {
    noStroke();
    fill(c);
    rectMode(CENTER);
    rect(pos.x, pos.y, r, r);
    rectMode(CORNER);
  }
}
class Player {
  PVector pos, old, vel;
  float r, grav, fric, speed, up, down, left, right, block, jumpPow, eyeSz, Scooldown, Pcooldown, health, Ptype, shootC, powC, Bcooldown, blockC, blocking, barX, barY, poison, mvt, 
    lerpHealth;
  boolean facingR, dead, falling;
  int col;
  float FH = height - 4*height/5; // floor height from bottom

  Player(float x, float y, boolean fr) {
    pos = new PVector(x, y);
    old = new PVector(x-1, y-1);
    vel = new PVector(0, 0);
    facingR = fr;
    r = PLAYER_SIZE;
    grav = PLAYER_GRAVITY;
    fric = 0.9f;
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


  public void update() { // =============================== UPDATE
    old = pos;

    if (poison > 0) {
      health -= POISON_DMG;
      poison --;
    }

    health += REGEN_RATE;

    if (Ptype == 1 || Ptype == 10) { // MACHINEGUN + DART
      shootC = 8;
    } else if (Ptype == 3) { // BURST
      shootC = 2.5f*SHOOT_COOLDOWN;
    } else if (Ptype == 5) { // GRENADE
      shootC = 2*SHOOT_COOLDOWN;
    } else if (Ptype == 6) { // BAZOOKA
      shootC = 3*SHOOT_COOLDOWN;
    } else if (Ptype == 7) { // LASER
      shootC = 2*SHOOT_COOLDOWN;
    } else if (Ptype == 8 || Ptype == 9) { // TRAMP + MINE
      shootC = 3.5f*SHOOT_COOLDOWN;
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
    
    lerpHealth = lerp(lerpHealth, health, 0.2f);
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
        vel.y = lerp(vel.y, -MOVE_SPEED*1.2f, 0.2f);
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
    
    mvt = lerp(mvt, (right - left) * speed, 0.3f);
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
  public void show(int c) {
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
            spawnFire(pos.x - r/4, pos.y + r, random(-0.1f, 0.1f), 1);
          }
          image(jetpack, pos.x - PLAYER_SIZE/2, pos.y, PLAYER_SIZE/2, PLAYER_SIZE);
        } else if (Ptype > 0) {
          image(Psprites[PApplet.parseInt(Ptype)], pos.x, pos.y - r/6, r, r);
        }

        rect(pos.x + r - eyeSz, pos.y + r/2, eyeSz, eyeSz);
      } else { // LEFT <<<<<<<<<<<<<<<<<<<<<<
        if (Ptype == 2) {
          if (up == 1) {
            spawnFire(pos.x + r + r/4, pos.y + r, random(-0.1f, 0.1f), 1);
          }
          image(jetpack, pos.x + PLAYER_SIZE, pos.y, PLAYER_SIZE/2, PLAYER_SIZE);
        } else if (Ptype > 0) {
          image(Psprites[PApplet.parseInt(Ptype)], pos.x, pos.y - r/6, r, r);
        }

        rect(pos.x, pos.y + r/2, eyeSz, eyeSz);
      }

      if (blocking > 0) {
        image(shield, pos.x - PLAYER_SIZE/10, pos.y - PLAYER_SIZE/10, PLAYER_SIZE + PLAYER_SIZE/5, PLAYER_SIZE + PLAYER_SIZE/5);
      }

      barX = lerp(barX, pos.x, 0.2f);
      barY = lerp(barY, pos.y, 0.2f);

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
        stroke(0xffAD00F2);
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
class Powerup {
  PVector pos;
  float r;
  int type;
  PImage sprite;

  Powerup(float x, float y, int t) {
    pos = new PVector(x, y);
    r = PLAYER_SIZE;
    type = t; // Types : 1=machinegun 2=jetpack 3=burst 4=heal 5=grenade 6=bazooka 7=lasergun 8=trampoline 9=mine 10=poisondart
    sprite = Psprites[type];
  }

  public void update() {
  }

  public void show() {
    image(spriteBG, pos.x, pos.y, r, r);
    image(sprite, pos.x, pos.y, r, r);
  }
}
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

  public void update() {

    if (facingR) {
      vel.x = lerp(vel.x, ROCKET_SPEED, 0.15f);
    } else {
      vel.x = lerp(vel.x, -ROCKET_SPEED, 0.15f);
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

  public void show() {
    image(sprite, pos.x, pos.y, r, r);
    spawnSmoke(pos.x + r/2, pos.y + r/2);
  }
}
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
  
  public void update() {
    life ++;
    
    if (life >= lifetime) {
      dead = true;
    }
  }
  
  public void show() {
    stroke(0);
    fill(0, 80, 0);
    image(tramp, pos.x, pos.y, r, EYE_SIZE);
  }
}
// CONTROLS \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
public void keyPressed() {
  if (key == 'w' || key == 'W') {
    p1.up = 1;
  }
  if (key == 'a' || key == 'A') {
    p1.left = 1;
  }
  if (key == 'f' || key == 'F') {
    p1.down = 1;
  }
  if (key == 'd' || key == 'D') {
    p1.right = 1;
  }
  if (key == 's') {
    p1.block = 1;
  }

  if (key == 'i' || key == 'I') {
    p2.up = 1;
  }
  if (key == 'j' || key == 'J') {
    p2.left = 1;
  }
  if (key == ';' || key == ':') {
    p2.down = 1;
  }
  if (key == 'l' || key == 'L') {
    p2.right = 1;
  }
  if (key == 'k') {
    p2.block = 1;
  }

  if (key == 'r' || key == 'R') {
    if (p1.dead || p2.dead) {
      reset();
      frameCount = 1;
    }
  }
}

public void keyReleased() {
  if (key == 'w') {
    p1.up = 0;
  }
  if (key == 'a') {
    p1.left = 0;
  }
  if (key == 'f') {
    p1.down = 0;
  }
  if (key == 'd') {
    p1.right = 0;
  }
  if (key == 's') {
    p1.block = 0;
  }

  if (key == 'i') {
    p2.up = 0;
  }
  if (key == 'j') {
    p2.left = 0;
  }
  if (key == ';') {
    p2.down = 0;
  }
  if (key == 'l') {
    p2.right = 0;
  }
  if (key == 'k') {
    p2.block = 0;
  }
}
public void spawnPowerup() {
  float x, y;
  int type;

  boolean onPlatform = PApplet.parseInt(random(3)) < 1;

  if (onPlatform) {
    x = random(width/4, width/4 + width/2 - PLAYER_SIZE);
    y = platformY - PLAYER_SIZE - 5;
  } else {
    x = random(width - PLAYER_SIZE);
    y = 4*height/5 - PLAYER_SIZE - 5;
  }

  type = PApplet.parseInt(random(10) + 1);

  powpop.trigger();
  Powerup powerup = new Powerup(x, y, type);
  powerups.add(powerup);
}

public void spawnBullet(float posx, float posy, boolean facingR, float num) {
  float x = posx;
  float y = posy;

  float dx;

  for (int i = 0; i < num; i++) {
    if (facingR) {
      dx = random(BULLET_SPEED - 2, BULLET_SPEED + 2);
    } else {
      dx = -random(BULLET_SPEED - 2, BULLET_SPEED + 2);
    }
    Bullet bullet = new Bullet(x, y, dx, num*random(-0.2f, 0.2f));
    bullets.add(bullet);
  }
}

public void bulletBurst(float x, float y, float num, float pow) {
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

public void spawnSmoke(float x, float y) {
  Particle p = new Particle(x, y, random(-0.1f, 0.1f), random(-0.9f, -1.1f), random(8, 16), color(100, 100), 150, 0, false);
  particles.add(p);
}

public void spawnFire(float x, float y, float dx, float dy) {
  int c;
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

public void laserBeam(float x, float y, boolean facingR) {
  if (facingR) {
    for (int i = PApplet.parseInt(x); i < width; i += 2) {
      Particle p = new Particle(i, y, 0, 0, random(4, 10), color(255, 0, 0, 100), 25, 1, true);
      particles.add(p);
    }
  } else {
    for (int i = PApplet.parseInt(x); i > 0; i -= 2) {
      Particle p = new Particle(i, y, 0, 0, random(4, 10), color(255, 0, 0, 100), 25, 1, true);
      particles.add(p);
    }
  }
  
  Particle p = new Particle(x, y, 0, 0, 200, color(255, 0, 0, 100), 20, 0, false);
  particles.add(p);
}
public void debug() {
  fill(0);
  float t = 21;
  textSize(20);
  text("bullets : " + bullets.size(), 10, height - t);
  text("particles : " + particles.size(), 10, height - 2*t);
  text("powerups : " + powerups.size(), 10, height - 3*t);

  textSize(15);
  text("FPS : " + PApplet.parseInt(frameRate), 10, 20);
}
  public void settings() {  fullScreen(P2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "GetRect" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
