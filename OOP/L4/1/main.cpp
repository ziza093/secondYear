#include "header.h"

int main() {

    Multimea m(10);
    m.add(4);
    m.add(3);
    m.write();
    m.remove(4);
    m.remove(4);
    m.write();
    m.add(9);
    m.add(2);
    m.add(2);
    m.write();

return 0;
}