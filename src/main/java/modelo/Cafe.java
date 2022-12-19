package modelo;

import java.util.Objects;

public class Cafe {
    
    String tipo, tamanio, fecha;
    double precio = 1.50;

    public Cafe(String tipo, String tamanio, double precio, String fecha) {
        this.tipo = tipo;
        this.tamanio = tamanio;
        this.fecha = fecha;
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "Cafe{" + "tipo=" + tipo + ", tamanio=" + tamanio + ", fecha=" + fecha + ", precio=" + precio + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Cafe other = (Cafe) obj;
        if (Double.doubleToLongBits(this.precio) != Double.doubleToLongBits(other.precio)) {
            return false;
        }
        if (!Objects.equals(this.tipo, other.tipo)) {
            return false;
        }
        if (!Objects.equals(this.tamanio, other.tamanio)) {
            return false;
        }
        return Objects.equals(this.fecha, other.fecha);
    }

    
    
    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setTamanio(String tamanio) {
        this.tamanio = tamanio;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public String getTamanio() {
        return tamanio;
    }

}
