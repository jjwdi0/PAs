int binary_search(int *arr, int s, int e, int value) {
  while(s <= e) {
    int mid = (s + e) >> 1;
    if(arr[mid] == value) {
      return mid;
    } else if(arr[mid] > value) {
      e = mid - 1;
    } else {
      s = mid + 1;
    }
  }
  return -1;
}
