void debug() {
  fill(0);
  float t = 21;
  textSize(20);
  text("bullets : " + bullets.size(), 10, height - t);
  text("particles : " + particles.size(), 10, height - 2*t);
  text("powerups : " + powerups.size(), 10, height - 3*t);

  textSize(15);
  text("FPS : " + int(frameRate), 10, 20);
}
