package com.mycompany.polloloco.dao;

import com.mycompany.polloloco.modelo.Mesa;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MesaDAO {

    public List<Mesa> listarDisponibles() {
        List<Mesa> lista = new ArrayList<>();
        String sql = "SELECT * FROM mesa WHERE estado = 'Libre'";
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Mesa(
                        rs.getInt("id"),
                        rs.getInt("numero"),
                        rs.getInt("capacidad"),
                        rs.getString("estado")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public boolean cambiarEstado(int idMesa, String nuevoEstado) {
        String sql = "UPDATE mesa SET estado=? WHERE id=?";
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idMesa);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
}
