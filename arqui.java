public class SegmentedControlUnitSimulation {

    public static void main(String[] args) {
    // Inicializar la unidad de control segmentado con dos segmentos
    SegmentedControlUnit scu = new SegmentedControlUnit(2);

    // Ejecutar instrucciones
    scu.executeInstruction("LOAD R1, 10"); // Carga el valor 10 en el registro R1
    scu.executeInstruction("ADD R2, 100, 5"); // Suma el valor en la dirección de memoria 100 y 5 y lo guarda en R2
    scu.executeInstruction("STORE R2, 100"); // Guarda el valor de R2 en la dirección de memoria 100

    // Mostrar el estado final de la unidad de control segmentado
    System.out.println(scu);
}

}

class SegmentedControlUnit {

    private int[] registers;
    private int[] memory;
    private int instructionPointer;
    private int numSegments;
    private int segmentSize;

    public SegmentedControlUnit(int numSegments) {
        this.numSegments = numSegments;
        this.memory = new int[256]; // Tamaño del arreglo debe ser al menos 256
        this.registers = new int[16];
        this.segmentSize = memory.length / numSegments;
        this.instructionPointer = 0;
    }

    public void executeInstruction(String instruction) {
        // Obtener el segmento actual
        int currentSegment = instructionPointer / segmentSize;
        int segmentStart = currentSegment * segmentSize;
        int segmentEnd = segmentStart + segmentSize;

        // Validar que la dirección de la instrucción esté dentro del segmento actual
        int instructionAddress = Integer.parseInt(instruction.split(",")[1].trim());
        if (instructionAddress < segmentStart || instructionAddress >= segmentEnd) {
            System.out.println("ERROR: Dirección de memoria fuera del segmento actual");
            return;
        }

        // Ejecutar la instrucción
        String[] parts = instruction.split(" ");
        String opcode = parts[0];
        String[] operands = parts[1].split(",");
        if (operands.length < 2) {
            System.out.println("ERROR: Operando inválido");
            return;
        }
        switch (opcode) {
            case "LOAD":
                int register1 = getRegisterNumber(operands[0]);
                int address = Integer.parseInt(operands[1].trim());
                registers[register1] = memory[address];
                instructionPointer++;
                break;
            case "STORE":
                int register2 = getRegisterNumber(operands[0]);
                int address2 = Integer.parseInt(operands[1].trim());
                memory[address2] = registers[register2];
                instructionPointer++;
                break;
            case "ADD":
                int register3 = getRegisterNumber(operands[0]);
                int register4 = getRegisterNumber(operands[1]);
                int value = Integer.parseInt(operands[2].trim());
                registers[register3] = registers[register4] + value;
                instructionPointer++;
                break;
            default:
                System.out.println("ERROR: Opcode desconocido");
                return;
        }
    }

    private int getRegisterNumber(String registerName) {
        switch (registerName) {
            case "R0":
                return 0;
            case "R1":
                return 1;
            case "R2":
                return 2;
            case "R3":
                return 3;
            case "R4":
                return 4;
            case "R5":
                return 5;
            case "R6":
                return 6;
            case "R7":
            return 7;
        case "R8":
            return 8;
        case "R9":
            return 9;
        case "R10":
            return 10;
        case "R11":
            return 11;
        case "R12":
            return 12;
        case "R13":
            return 13;
        case "R14":
            return 14;
        case "R15":
            return 15;
        default:
            System.out.println("ERROR: Nombre de registro desconocido");
            return -1;
    }
}

public String toString() {
    StringBuilder sb = new StringBuilder();
    
    sb.append("Registros:\n");
    for (int i = 0; i < registers.length; i++) {
        sb.append(String.format("R%d: %d\n", i, registers[i]));
    }
    
    sb.append("\nMemoria:\n");
    for (int i = 0; i < memory.length; i++) {
        sb.append(String.format("%d: %d\n", i, memory[i]));
    }
    
    sb.append("\nIP: ").append(instructionPointer).append("\n");
    
    return sb.toString();
}
}