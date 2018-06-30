import ddf.minim.*;

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
color P1Color = color(180, 50, 50);
color P2Color = color(50, 50, 180);
color floorColor = color(180);

float INITIAL_COOLDOWN = 80; // Time at beginning of game before anyone can shoot ; Frames
float PLAYER_SIZE = 40; // Size of players and powerups ; Pixels
float PLAYER_HEALTH = 100; // Arbitrary unit
float BULLET_DAMAGE = 10; // Self explanatory
float REGEN_RATE = 0.03; // Health per frame regained by both players
float EYE_SIZE = PLAYER_SIZE/4; // Size of eyes & bullets + height of trampolines & mines ; Pixels
float MOVE_SPEED = 8; // Speed of players ; Pixels per second
float WALL_SPEED = 2; // Speed of players while on wall ; Pixels per second
float JUMP_POW = 12; // Player jump power ; Vertical velocity (pixels per second) ; Also affects wall jump power
float THROW_POW = 13; // Player grenade throw power ; Horizontal velocity
float BULLET_SPEED = 18; // Speed of bullets ; Pixels per second
float BULLET_NUMBER = 1; // Number of bullets in a normal shot
float PLAYER_GRAVITY = 0.45; // Gravity of player ; Pixels down added to acceleration per second
float BULLET_GRAVITY = 0; // Gravity of bullets ; Pixels down added to acceleration per second
float SHOOT_COOLDOWN = 20; // Time, per player, between each shot ; Frames
float POWERUP_COOLDOWN = 500; // Time bewteen each powerup spawn ; Frames 
float POWERUP_DURATION = 450; // Time a powerup lasts once picked up ; Frames
float EVENT_COOLDOWN = 1300; // Time between each event ; Frames
float EVENT_SIZE = 50; // Number of bullets shot from above
float FLOOR_WIDTH = 70; // Thickness of floor platform (platform scales) ; Pixels
float GRENADE_TIME = 120; // Time before a grenade detonates ; Frames
float GRENADE_BOUNCE = 0.7; // Amoun of bounciness ; Proportion of last bounce to current bounce
float FIRE_DMG = 2; // Damage done by fire to players ; Damage per frame
float ROCKET_SPEED = 16; // Speed of bazooka rockets ; Pixels per second
float BLOCK_COOLDOWN = 80; // Time between each block ; Frames
float BLOCK_DURATION = 25; // Time a block lasts ; Frames
float ROCKET_RECOIL = 21; // Recoil of bazooka powerup on shoot ; Vertical velocity
float LASER_RECOIL = 15; // Recoil of laser powerup on shoot ; Vertical velocity
float TRAMP_DURATION = 1600; // Duration of a trampoline before it disappears ; Frames
float POISON_DMG = 0.2; // Damage dealt when poisoned ; Damage per frame
float POISON_TIME = 300; // Duration of poison ; Frames
float DART_SPEED = 11;

float platformX;
float platformY;

// ===========================================================================================

void reset() {
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

void event() {
  for (int i = 0; i < EVENT_SIZE; i++) {
    float x = random(10, width - 10);
    Bullet bullet = new Bullet(x, 1, random(-2, 2), random(3, 8));
    bullets.add(bullet);
  }
}

// SETUP +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
void setup() {
  fullScreen(P2D);
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
void draw() {
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


void stop() {
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
