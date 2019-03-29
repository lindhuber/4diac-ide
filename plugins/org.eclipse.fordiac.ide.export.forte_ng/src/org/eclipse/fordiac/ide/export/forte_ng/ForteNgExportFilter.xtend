/*******************************************************************************
 * Copyright (c) 2019 fortiss GmbH
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Martin Jobst
 *     - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.fordiac.ide.export.forte_ng

import java.nio.file.Paths
import org.eclipse.fordiac.ide.export.TemplateExportFilter
import org.eclipse.fordiac.ide.export.forte_ng.adapter.AdapterFBHeaderTemplate
import org.eclipse.fordiac.ide.export.forte_ng.adapter.AdapterFBImplTemplate
import org.eclipse.fordiac.ide.export.forte_ng.basic.BasicFBHeaderTemplate
import org.eclipse.fordiac.ide.export.forte_ng.basic.BasicFBImplTemplate
import org.eclipse.fordiac.ide.export.forte_ng.composite.CompositeFBHeaderTemplate
import org.eclipse.fordiac.ide.export.forte_ng.composite.CompositeFBImplTemplate
import org.eclipse.fordiac.ide.export.forte_ng.service.ServiceInterfaceFBHeaderTemplate
import org.eclipse.fordiac.ide.export.forte_ng.service.ServiceInterfaceFBImplTemplate
import org.eclipse.fordiac.ide.model.libraryElement.AdapterType
import org.eclipse.fordiac.ide.model.libraryElement.BasicFBType
import org.eclipse.fordiac.ide.model.libraryElement.CompositeFBType
import org.eclipse.fordiac.ide.model.libraryElement.LibraryElement
import org.eclipse.fordiac.ide.model.libraryElement.ServiceInterfaceFBType

class ForteNgExportFilter extends TemplateExportFilter {

	override protected getTemplates(LibraryElement type) {
		return switch (type) {
			BasicFBType:
				#{
					new BasicFBHeaderTemplate(type, '''«type.name».h''', Paths.get("")),
					new BasicFBImplTemplate(type, '''«type.name».cpp''', Paths.get(""))
				}
			CompositeFBType:
				#{
					new CompositeFBHeaderTemplate(type, '''«type.name».h''', Paths.get("")),
					new CompositeFBImplTemplate(type, '''«type.name».cpp''', Paths.get(""))
				}
			AdapterType:
				#{
					new AdapterFBHeaderTemplate(type.adapterFBType, '''«type.name».h''', Paths.get("")),
					new AdapterFBImplTemplate(type.adapterFBType, '''«type.name».cpp''', Paths.get(""))
				}
			ServiceInterfaceFBType:
				#{
					new ServiceInterfaceFBHeaderTemplate(type, '''«type.name».h''', Paths.get("")),
					new ServiceInterfaceFBImplTemplate(type, '''«type.name».cpp''', Paths.get(""))
				}
			default: {
				errors.add('''Unknown library element type «type.class.name»''')
				emptySet
			}
		}
	}

}
