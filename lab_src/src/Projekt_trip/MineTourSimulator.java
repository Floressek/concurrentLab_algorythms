package src.Projekt_trip;

public class MineTourSimulator {
    public static void main(String[] args) {
        Room[] rooms = {
                new Room(10, 1),  // Room 1
                new Room(20, 2),  // Room 2 (double capacity)
                new Room(10, 3),  // Room 3
                new Room(10, 4)   // Room 4
        };

        Passage[] passages = new Passage[3];
        for (int i = 0; i < passages.length; i++) {
            passages[i] = new Passage(); // Passage 1, 2, 3 index
        }

        // Routes
        Room[] routeA = {rooms[0], rooms[1], rooms[2], rooms[3]};
        Room[] routeB = {rooms[3], rooms[2], rooms[1], rooms[0]};
        Passage[] passagesA = {passages[0], passages[1], passages[2]};
        Passage[] passagesB = {passages[2], passages[1], passages[0]};

        Thread touristA = new Thread(new Tourist("Tourist A", routeA, passagesA));
        Thread touristB = new Thread(new Tourist("Tourist B", routeB, passagesB));
        Thread touristC = new Thread(new Tourist("Tourist C", routeA, passagesA));
        Thread touristD = new Thread(new Tourist("Tourist D", routeB, passagesB));
        Thread touristE = new Thread(new Tourist("Tourist E", routeA, passagesA));

        touristA.start();
        touristB.start();
//        touristC.start();
//        touristD.start();
//        touristE.start();
    }
}

