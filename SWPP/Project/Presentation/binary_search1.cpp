int binary_search(int *arr, int s, int e, int value) {
  if(s > e) return -1;
  int mid = (s + e) / 2;
  if(arr[mid] == value) {
    return mid; 
  } else if(arr[mid] > value) {
    e = mid - 1;
  } else {
    s = mid + 1;
  }
  return binary_search(arr, s, e, value);
}
