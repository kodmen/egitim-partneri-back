import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DersComponent } from './list/ders.component';
import { DersDetailComponent } from './detail/ders-detail.component';
import { DersUpdateComponent } from './update/ders-update.component';
import { DersDeleteDialogComponent } from './delete/ders-delete-dialog.component';
import { DersRoutingModule } from './route/ders-routing.module';

@NgModule({
  imports: [SharedModule, DersRoutingModule],
  declarations: [DersComponent, DersDetailComponent, DersUpdateComponent, DersDeleteDialogComponent],
  entryComponents: [DersDeleteDialogComponent],
})
export class DersModule {}
