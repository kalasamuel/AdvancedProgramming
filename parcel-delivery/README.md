# Local Parcel Delivery - DDD, TDD & Clean Architecture (Java)

Group 8 coursework. Java 17+, JUnit 5, Maven. Everything is in memory (no database, no web UI).

## Command to run the tests
    mvn test
(Demo of both use cases: run `parcel.interfaces.Main` from your IDE, or `mvn compile exec:java -Dexec.mainClass=parcel.interfaces.Main`.)

## Layers (source folder = Clean Architecture area)
    src/main/java/parcel/
      domain/          Entity (layer supertype), Delivery, Courier, DeliveryAddress, ParcelPickedUpEvent,
                       DeliveryAssignmentService, DeliveryStatus, CourierStatus, DomainEvent, DomainException
                       -> imports nothing from the other layers
      application/     AssignDeliveryService (UC1), PickupDeliveryService (UC2), DTOs, IDeliveryRepository,
                       ICourierRepository, EventDispatcher, CourierWorkloadHandler, NotFoundException
                       -> imports domain only
      infrastructure/  InMemoryDeliveryRepository, InMemoryCourierRepository, InMemoryEventDispatcher
                       -> imports application + domain
      interfaces/      Main (minimal entry point; also where dependency injection is wired)
    src/test/java/parcel/   T1-T8 (exactly eight tests)

## Use cases (connected by ParcelPickedUpEvent)
UC1 Assign Delivery (AssignDeliveryService) -> UC2 Pick Up Delivery (PickupDeliveryService)
Request -> PickupDeliveryService -> Delivery.markPickedUp() -> ParcelPickedUpEvent -> CourierWorkloadHandler -> Courier.recordActiveDelivery()

## Traceability
| Rule | Concept            | Code                                   | Test | Result                         |
|------|--------------------|----------------------------------------|------|--------------------------------|
| BR1  | Value Object       | DeliveryAddress                        | T1   | IllegalArgumentException       |
| BR2  | Entity / Root (state rule) | Delivery.assign(), markPickedUp()   | T2   | DomainException, state unchanged |
| BR3  | Aggregate invariant | Delivery constructor (pickup != destination) | T3 | DomainException, delivery not created |
| BR4  | Domain Service     | DeliveryAssignmentService              | T4   | returns false                  |
| BR5  | Domain Event       | ParcelPickedUpEvent + CourierWorkloadHandler | T5 | event recorded (T7: handled) |
| BR6  | Repository + App Service | IDeliveryRepository + PickupDeliveryService | T6 | NotFoundException |
| -    | Main use case      | UC1 + UC2 + event handler              | T7   | PICKED_UP, courier BUSY        |
| -    | Aggregate B rule   | Courier.recordActiveDelivery()         | T8   | DomainException, state kept    |

## TDD evidence (T1 / BR1) - see /evidence
1. tdd-1-red-failing-test.txt   : T1 written first, no validation -> FAILS
2. tdd-2-green-passing-test.txt : validation added in DeliveryAddress -> PASSES
3. full-test-run.txt            : all eight tests pass
To reproduce on your own machine: comment out the two `if (...) throw` blocks in DeliveryAddress,
run `mvn test -Dtest=DeliveryAddressTest` (red), restore them, run it again (green), and save both outputs.

## AI use
<TOOL NAME, e.g. Claude (Anthropic)> was used to <what it helped with, e.g. draft the Java implementation and
test skeletons>. The group reviewed it and can explain every design decision.
(Edit this line so it is true for your group.)
