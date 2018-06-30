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

  void update() {
  }

  void show() {
    image(spriteBG, pos.x, pos.y, r, r);
    image(sprite, pos.x, pos.y, r, r);
  }
}
