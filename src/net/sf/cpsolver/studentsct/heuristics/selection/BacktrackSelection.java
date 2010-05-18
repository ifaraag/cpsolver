package net.sf.cpsolver.studentsct.heuristics.selection;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;

import net.sf.cpsolver.ifs.heuristics.NeighbourSelection;
import net.sf.cpsolver.ifs.model.Neighbour;
import net.sf.cpsolver.ifs.solution.Solution;
import net.sf.cpsolver.ifs.solver.Solver;
import net.sf.cpsolver.ifs.util.DataProperties;
import net.sf.cpsolver.ifs.util.Progress;
import net.sf.cpsolver.studentsct.heuristics.RandomizedBacktrackNeighbourSelection;
import net.sf.cpsolver.studentsct.model.Enrollment;
import net.sf.cpsolver.studentsct.model.Request;

/**
 * Use backtrack neighbour selection. For all unassigned variables (in a random
 * order), {@link RandomizedBacktrackNeighbourSelection} is being used.
 * 
 * <br>
 * <br>
 * 
 * @version StudentSct 1.2 (Student Sectioning)<br>
 *          Copyright (C) 2007 - 2010 Tomas Muller<br>
 *          <a href="mailto:muller@unitime.org">muller@unitime.org</a><br>
 *          Lazenska 391, 76314 Zlin, Czech Republic<br>
 * <br>
 *          This library is free software; you can redistribute it and/or modify
 *          it under the terms of the GNU Lesser General Public License as
 *          published by the Free Software Foundation; either version 2.1 of the
 *          License, or (at your option) any later version. <br>
 * <br>
 *          This library is distributed in the hope that it will be useful, but
 *          WITHOUT ANY WARRANTY; without even the implied warranty of
 *          MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *          Lesser General Public License for more details. <br>
 * <br>
 *          You should have received a copy of the GNU Lesser General Public
 *          License along with this library; if not, write to the Free Software
 *          Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 *          02110-1301 USA
 */

public class BacktrackSelection implements NeighbourSelection<Request, Enrollment> {
    private RandomizedBacktrackNeighbourSelection iRBtNSel = null;
    private Enumeration<Request> iRequestEnumeration = null;

    public BacktrackSelection(DataProperties properties) {
    }

    public void init(Solver<Request, Enrollment> solver, String name) {
        Vector<Request> unassigned = new Vector<Request>(solver.currentSolution().getModel().unassignedVariables());
        Collections.shuffle(unassigned);
        iRequestEnumeration = unassigned.elements();
        if (iRBtNSel == null) {
            try {
                iRBtNSel = new RandomizedBacktrackNeighbourSelection(solver.getProperties());
                iRBtNSel.init(solver);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        Progress.getInstance(solver.currentSolution().getModel()).setPhase(name, unassigned.size());
    }

    public void init(Solver<Request, Enrollment> solver) {
        init(solver, "Backtracking...");
    }

    public Neighbour<Request, Enrollment> selectNeighbour(Solution<Request, Enrollment> solution) {
        while (iRequestEnumeration.hasMoreElements()) {
            Request request = iRequestEnumeration.nextElement();
            Progress.getInstance(solution.getModel()).incProgress();
            Neighbour<Request, Enrollment> n = iRBtNSel.selectNeighbour(solution, request);
            if (n != null)
                return n;
        }
        return null;
    }

}
