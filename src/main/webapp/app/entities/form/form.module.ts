import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FormComponent } from './list/form.component';
import { FormDetailComponent } from './detail/form-detail.component';
import { FormUpdateComponent } from './update/form-update.component';
import { FormDeleteDialogComponent } from './delete/form-delete-dialog.component';
import { FormRoutingModule } from './route/form-routing.module';

@NgModule({
  imports: [SharedModule, FormRoutingModule],
  declarations: [FormComponent, FormDetailComponent, FormUpdateComponent, FormDeleteDialogComponent],
  entryComponents: [FormDeleteDialogComponent],
})
export class FormModule {}
