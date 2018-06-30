// CONTROLS \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
void keyPressed() {
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

void keyReleased() {
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
