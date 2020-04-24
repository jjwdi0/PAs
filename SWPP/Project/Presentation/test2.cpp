int sum(int n) {
  int cnt = 0;
  while(n) {
    cnt += n;
    n--;
  }
  return cnt;
}
