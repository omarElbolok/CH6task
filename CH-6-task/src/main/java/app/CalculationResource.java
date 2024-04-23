package app;

import ejb.Calculation;
import ejb.CalculationService;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/calc")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CalculationResource {

    @EJB
    private CalculationService calculationService;

    public static class CalculationRequest {
        private int number1;
        private int number2;
        private String operation;

        // Getters and setters
        public int getNumber1() {
            return number1;
        }

        public void setNumber1(int number1) {
            this.number1 = number1;
        }

        public int getNumber2() {
            return number2;
        }

        public void setNumber2(int number2) {
            this.number2 = number2;
        }

        public String getOperation() {
            return operation;
        }

        public void setOperation(String operation) {
            this.operation = operation;
        }
    }

    public static class CalculationResponse {
        private double result;

        public CalculationResponse(double result) {
            this.result = result;
        }

        // Getters and setters
        public double getResult() {
            return result;
        }

        public void setResult(double result) {
            this.result = result;
        }
    }

    @POST
    public Response createCalculation(CalculationRequest request) {
        try {
            int number1 = request.getNumber1();
            int number2 = request.getNumber2();
            String operation = request.getOperation();

            double result = calculationService.performCalculation(number1, number2, operation);
            calculationService.saveCalculation(number1, number2, operation, result);

            return Response.ok(new CalculationResponse(result)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/calculations")
    public Response getAllCalculations() {
        try {
            List<Calculation> calculations = calculationService.getAllCalculations();
            return Response.ok(calculations).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}